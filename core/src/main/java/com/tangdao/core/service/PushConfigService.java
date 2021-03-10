package com.tangdao.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.RedisConstant;
import com.tangdao.core.context.SettingsContext.PushConfigStatus;
import com.tangdao.core.dao.PushConfigMapper;
import com.tangdao.core.model.domain.PushConfig;

import cn.hutool.core.collection.CollUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class PushConfigService extends BaseService<PushConfigMapper, PushConfig> {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private String getAssistKey(String userId, int type) {
		return String.format("%s:%s:%d", RedisConstant.RED_USER_PUSH_CONFIG, userId, type);
	}

	public boolean update(PushConfig record) {
		if (record.getId() == null) {
			return false;
		}

		pushToRedis(record.getUserId(), record.getType(), record);

		return this.baseMapper.updateById(record) > 0;
	}

	public List<PushConfig> findByUserId(String userId) {
		return this.list(Wrappers.<PushConfig>lambdaQuery().eq(PushConfig::getUserId, userId));
	}

	public boolean save(PushConfig record) {
		int id = this.baseMapper.insert(record);
		PushConfig c = selectByUserIdAndType(record.getUserId(), record.getType());
		record.setId(c.getId());
		pushToRedis(record.getUserId(), record.getType(), record);
		return id > 0;
	}

	public PushConfig getByUserId(String userId, int type) {
		try {
			Object object = stringRedisTemplate.opsForValue().get(getAssistKey(userId, type));
			if (object != null) {
				return JSON.parseObject(object.toString(), PushConfig.class);
			}

		} catch (Exception e) {
			logger.warn("REDIS 查询推送配置失败", e);
		}
		return selectByUserIdAndType(userId, type);
	}

	public PushConfig getPushUrl(String userId, int callbackUrlType, String customUrl) {
		PushConfig config = getByUserId(userId, callbackUrlType);
		if (config == null) {
			return null;
		}

		if (Integer.parseInt(config.getStatus()) == PushConfigStatus.YES_WITH_POST.getCode()) {
			config.setUrl(customUrl);
		}

		return config;
	}

	/**
	 * TODO 添加到REDIS
	 * 
	 * @param userCode
	 * @param type
	 * @param pc
	 */
	private void pushToRedis(String userId, int type, PushConfig pc) {
		try {
			stringRedisTemplate.opsForValue().set(getAssistKey(userId, type), JSON.toJSONString(pc));
		} catch (Exception e) {
			logger.warn("REDIS 推送配置操作失败", e);
		}
	}

	public boolean reloadToRedis() {
		List<PushConfig> list = this.list();
		if (CollUtil.isEmpty(list)) {
			logger.warn("用户推送设置数据查询为空");
			return false;
		}

		List<Object> con = stringRedisTemplate.execute((connection) -> {

			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (PushConfig config : list) {
				byte[] key = serializer.serialize(getAssistKey(config.getUserId(), config.getType()));
				connection.set(key, serializer.serialize(JSON.toJSONString(config)));
			}

			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

	public int updateByUserId(PushConfig pushConFig) {
		int result = 0;
		try {
			// 判断如果用户推送记录中没有数据，则插入推送信息
			PushConfig pushConfig = selectByUserIdAndType(pushConFig.getUserId(), pushConFig.getType());
			if (pushConfig == null) {
				result = this.baseMapper.insert(pushConFig);
			}
			result = this.baseMapper.updateByUserId(pushConFig);

			// 查询修改后数据存储到缓存中
			PushConfig cf = selectByUserIdAndType(pushConFig.getUserId(), pushConFig.getType());
			if (cf == null) {
				{
					return 0;
				}
			}

			pushToRedis(cf.getUserId(), cf.getType(), cf);
		} catch (Exception e) {
			logger.error("更新用户推送信息失败：{}", JSON.toJSONString(pushConFig), e);
		}

		return result;
	}

	public PushConfig selectByUserIdAndType(String userId, int type) {
		QueryWrapper<PushConfig> queryWrapper = new QueryWrapper<PushConfig>();
		queryWrapper.eq("user_id", userId);
		queryWrapper.eq("type", type);
		queryWrapper.orderByDesc("id");
		queryWrapper.last("limit 1");
		return this.getOne(queryWrapper);
	}
}