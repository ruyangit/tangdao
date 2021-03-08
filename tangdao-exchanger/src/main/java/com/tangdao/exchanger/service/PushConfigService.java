package com.tangdao.exchanger.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.CommonRedisConstant;
import com.tangdao.core.context.SettingsContext.PushConfigStatus;
import com.tangdao.core.model.domain.paas.PushConfig;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.PushConfigMapper;

import cn.hutool.core.collection.CollUtil;

@Service
public class PushConfigService extends BaseService<PushConfigMapper, PushConfig> {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String getAssistKey(String userCode, int type) {
		return String.format("%s:%s:%d", CommonRedisConstant.RED_USER_PUSH_CONFIG, userCode, type);
	}

	public boolean update(PushConfig record) {
		if (record.getId() == null) {
			return false;
		}

		pushToRedis(record.getUserCode(), record.getType(), record);

		return this.baseMapper.updateById(record) > 0;
	}

	public List<PushConfig> findByUserCode(String userCode) {
		return this.list(Wrappers.<PushConfig>lambdaQuery().eq(PushConfig::getUserCode, userCode));
	}

	public boolean save(PushConfig record) {
		int id = this.baseMapper.insert(record);
		PushConfig c = selectByUserCodeAndType(record.getUserCode(), record.getType());
		record.setId(c.getId());
		pushToRedis(record.getUserCode(), record.getType(), record);
		return id > 0;
	}

	public PushConfig getByUserCode(String userCode, int type) {
		try {
			Object object = stringRedisTemplate.opsForValue().get(getAssistKey(userCode, type));
			if (object != null) {
				return JSON.parseObject(object.toString(), PushConfig.class);
			}

		} catch (Exception e) {
			logger.warn("REDIS 查询推送配置失败", e);
		}
		return selectByUserCodeAndType(userCode, type);
	}

	public PushConfig getPushUrl(String userCode, int callbackUrlType, String customUrl) {
		PushConfig config = getByUserCode(userCode, callbackUrlType);
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
	private void pushToRedis(String userCode, int type, PushConfig pc) {
		try {
			stringRedisTemplate.opsForValue().set(getAssistKey(userCode, type), JSON.toJSONString(pc));
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
				byte[] key = serializer.serialize(getAssistKey(config.getUserCode(), config.getType()));
				connection.set(key, serializer.serialize(JSON.toJSONString(config)));
			}

			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

	public int updateByUserCode(PushConfig pushConFig) {
		int result = 0;
		try {
			// 判断如果用户推送记录中没有数据，则插入推送信息
			PushConfig pushConfig = selectByUserCodeAndType(pushConFig.getUserCode(), pushConFig.getType());
			if (pushConfig == null) {
				result = this.baseMapper.insert(pushConFig);
			}
			result = this.baseMapper.updateByUserCode(pushConFig);

			// 查询修改后数据存储到缓存中
			PushConfig cf = selectByUserCodeAndType(pushConFig.getUserCode(), pushConFig.getType());
			if (cf == null) {
				{
					return 0;
				}
			}

			pushToRedis(cf.getUserCode(), cf.getType(), cf);
		} catch (Exception e) {
			logger.error("更新用户推送信息失败：{}", JSON.toJSONString(pushConFig), e);
		}

		return result;
	}

	public PushConfig selectByUserCodeAndType(String userCode, int type) {
		QueryWrapper<PushConfig> queryWrapper = new QueryWrapper<PushConfig>();
		queryWrapper.eq("user_code", userCode);
		queryWrapper.eq("type", type);
		queryWrapper.orderByDesc("id");
		queryWrapper.last("limit 1");
		return this.getOne(queryWrapper);
	}
}