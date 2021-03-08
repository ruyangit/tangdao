package com.tangdao.exchanger.service;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.CommonRedisConstant;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.context.SettingsContext.UserDefaultPassageGroupKey;
import com.tangdao.core.model.domain.paas.UserPassage;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.UserPassageMapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

@Service
public class UserPassageService extends BaseService<UserPassageMapper, UserPassage>{

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	
	public List<UserPassage> findByUserId(String userId) {
		return this.list(Wrappers.<UserPassage>lambdaQuery().eq(UserPassage::getUserId, userId));
	}

	private String getKey(String userId, int type) {
		return String.format("%s:%d:%d", CommonRedisConstant.RED_USER_SMS_PASSAGE, userId, type);
	}

	
	public String getByUserCodeAndType(String userId, int type) {
		if (StrUtil.isEmpty(userId) || type == 0) {
			return null;
		}

		try {
			String passageGroupId = stringRedisTemplate.opsForValue().get(getKey(userId, type));
			if (StrUtil.isNotBlank(passageGroupId))
				return passageGroupId;

		} catch (Exception e) {
			logger.warn("REDIS中查询用户通道信息失败 ：{}", e.getMessage());
		}
		QueryWrapper<UserPassage> queryWrapper = new QueryWrapper<UserPassage>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("type", type);
		queryWrapper.orderByDesc("id").last(" limit 1");
		UserPassage userPassage = this.getOne(queryWrapper);
		if (userPassage == null) {
			return null;
		}

		return userPassage.getPassageGroupId();
	}

	
	public List<UserPassage> getPassageGroupListByGroupId(String passageGroupId) {
		return this.list(Wrappers.<UserPassage>lambdaQuery().eq(UserPassage::getPassageGroupId, passageGroupId));
	}

	
	public boolean save(String userId, UserPassage userPassage) {
		try {
			userPassage.setUserId(userId);
			userPassage.setCreateDate(new Date());
			return super.save(userPassage);
		} catch (Exception e) {
			logger.error("添加用户通道错误，{}", e);
			return false;
		}
	}

	/**
	 * TODO 保存用户通道组配置
	 * 
	 * @param passageGroupId 通道组ID
	 * @param userId         用户ID
	 * @param type           平台类型：短信/语音/...
	 */
	private void save(String passageGroupId, String userId, Integer type) {
		try {
			if (passageGroupId == null || userId == null || type == null) {
				logger.error("插入用户通道失败，passageGroupId: {}, 用戶:{}, type: {}", passageGroupId, userId, type);
				return;
			}
			UserPassage userPassage = new UserPassage();
			userPassage.setPassageGroupId(passageGroupId);
			userPassage.setType(type);
			userPassage.setUserId(userId);
			userPassage.setCreateDate(new Date());

			boolean effect = super.save(userPassage);
			if (effect) {
				pushToRedis(userPassage);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public boolean initUserPassage(String userCode, List<UserPassage> passageList) {
		try {
			if (CollUtil.isEmpty(passageList)) {
				// 如果传递的用户通道集合为空，则根据系统参数配置查询平台所有业务的默认可用通道信息，插入值用户通道关系表中
//                List<SystemConfig> systemConfigs = systemConfigService.findByType(SystemConfigType.USER_DEFAULT_PASSAGE_GROUP.name());
				List<DictData> dictTypes = DictUtils.getDictList(SystemConfigType.USER_DEFAULT_PASSAGE_GROUP.name());
				if (CollUtil.isEmpty(dictTypes)) {
					throw new RuntimeException("没有可用默认通道组，请配置");
				}

				Integer type = null;
				for (DictData map : dictTypes) {
					if (UserDefaultPassageGroupKey.SMS_DEFAULT_PASSAGE_GROUP.name()
							.equalsIgnoreCase(ObjectUtils.toString2(map.getDictLabel()))) {
						type = PlatformType.SEND_MESSAGE_SERVICE.getCode();
					} else if (UserDefaultPassageGroupKey.FS_DEFAULT_PASSAGE_GROUP.name()
							.equalsIgnoreCase(ObjectUtils.toString2(map.getDictLabel()))) {
						type = PlatformType.FLUX_SERVICE.getCode();
					} else if (UserDefaultPassageGroupKey.VS_DEFAULT_PASSAGE_GROUP.name()
							.equalsIgnoreCase(ObjectUtils.toString2(map.getDictLabel()))) {
						type = PlatformType.VOICE_SERVICE.getCode();
					}

					save(ObjectUtils.toString2(map.getDictValue()), userCode, type);
				}
				return true;
			}

			List<Integer> busiCodes = PlatformType.allCodes();
			// 如果传递的通道和不为空，则遍历传递的通道信息，并对平台所有业务代码进行 差值比较
			for (UserPassage passage : passageList) {
				save(passage.getPassageGroupId(), userCode, passage.getType());
				busiCodes.remove(passage.getType());
			}

			// busiCodes 为空则表明，传递的通道集合包含平台所有业务的通道信息，无需补齐
			if (CollUtil.isEmpty(busiCodes)) {
				return true;
			}

			// 如果此值不为空，则表明该业务没有设置通道，需要查询是否存在默认通道
			for (Integer code : busiCodes) {
				String key = UserDefaultPassageGroupKey.key(code);
				if (StrUtil.isEmpty(key)) {
					continue;
				}

				String passageGroupId = DictUtils.getDictValue(SystemConfigType.USER_DEFAULT_PASSAGE_GROUP.name(), key,
						null);
//                SystemConfig config = systemConfigService.findByTypeAndKey(SysDictType.USER_DEFAULT_PASSAGE_GROUP.name(), key);
				if (passageGroupId == null) {
					logger.warn("没有可用默认通道组，请配置");
					continue;
				}

				save(passageGroupId, userCode, code);
			}

			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	@Transactional(readOnly = false, rollbackFor = RuntimeException.class)
	public boolean save(String userCode, List<UserPassage> userPassages) {
		if (CollUtil.isEmpty(userPassages)) {
			return false;
		}

		try {
			for (UserPassage userPassage : userPassages) {
				save(userCode, userPassage);
				pushToRedis(userPassage);
			}

			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public boolean update(String userCode, int type, String passageGroupId) {
		int effect = this.getBaseMapper().updateByUserCodeAndType(passageGroupId, userCode,
				PlatformType.SEND_MESSAGE_SERVICE.getCode());
		if (effect > 0) {
			pushToRedis(new UserPassage(userCode, type, passageGroupId));
		}

		return true;
	}

	private boolean pushToRedis(UserPassage userPassage) {
		try {
			stringRedisTemplate.opsForValue().set(getKey(userPassage.getUserCode(), userPassage.getType()),
					userPassage.getPassageGroupId());
			return true;
		} catch (Exception e) {
			logger.warn("REDIS 加载用户通道组配置数据失败", e);
			return false;
		}
	}

	
	public boolean reloadModelToRedis() {
		List<UserPassage> list = super.list();
		if (CollUtil.isEmpty(list)) {
			logger.warn("可用用户通道组数据为空");
			return false;
		}

		stringRedisTemplate.execute((connection) -> {

			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (UserPassage userPassage : list) {
				byte[] key = serializer.serialize(getKey(userPassage.getUserCode(), userPassage.getType()));

				connection.set(key, serializer.serialize(JSON.toJSONString(userPassage)));
			}

			return connection.closePipeline();

		}, false, true);

		return true;
	}
}