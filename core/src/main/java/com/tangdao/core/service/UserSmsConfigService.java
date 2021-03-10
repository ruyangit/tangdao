package com.tangdao.core.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.RedisConstant;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.dao.UserSmsConfigMapper;
import com.tangdao.core.model.domain.UserSmsConfig;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

public class UserSmsConfigService extends BaseService<UserSmsConfigMapper, UserSmsConfig> {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private static String getKey(String userId) {
		return RedisConstant.RED_USER_SMS_CONFIG + ":" + userId;
	}

	public UserSmsConfig getByUserId(String userId) {
		if (StrUtil.isEmpty(userId)) {
			return null;
		}

		try {
			String text = stringRedisTemplate.opsForValue().get(getKey(userId));
			if (StrUtil.isNotEmpty(text)) {
				return JSON.parseObject(text, UserSmsConfig.class);
			}

		} catch (Exception e) {
			logger.warn("REDIS获取用户短信配置失败", e);
		}

		return this.getOne(Wrappers.<UserSmsConfig>lambdaQuery().eq(UserSmsConfig::getUserId, userId));
	}

	public int getSingleChars(String userId) {
		int wordsPerNum = UserBalanceConstant.WORDS_SIZE_PER_NUM;
		try {
			UserSmsConfig userSmsConfig = getByUserId(userId);
			if (userSmsConfig != null) {
				wordsPerNum = userSmsConfig.getSmsWords();
			}

		} catch (Exception e) {
			logger.warn("用戶：{} 短信字数配置失败，将以默认每条字数：{}计费", userId, wordsPerNum, e);
		}
		return wordsPerNum;
	}

	private UserSmsConfig defaultConfig() {
		UserSmsConfig userSmsConfig = new UserSmsConfig();
		userSmsConfig.setCreateDate(new Date());
		userSmsConfig.setAutoTemplate("0");
		userSmsConfig.setMessagePass("0");
		userSmsConfig.setNeedTemplate("1");
		userSmsConfig.setSmsReturnRule("0");
		userSmsConfig.setSmsTimeout(0L);

		return userSmsConfig;
	}

	public boolean save(String userId, int words, String extNumber) {
		UserSmsConfig userSmsConfig = defaultConfig();
		userSmsConfig.setUserId(userId);
		userSmsConfig.setExtNumber(extNumber);
		userSmsConfig.setSmsWords(words);

		return save(userSmsConfig);
	}

	public boolean update(UserSmsConfig config) {
		if (super.updateById(config)) {
			pushToRedis(config);
			return true;
		}
		return false;
	}

	/**
	 * TODO 添加至REDIS
	 *
	 * @param userSmsConfig
	 */
	private void pushToRedis(UserSmsConfig userSmsConfig) {
		try {
			stringRedisTemplate.opsForValue().set(getKey(userSmsConfig.getUserId()), JSON.toJSONString(userSmsConfig));
		} catch (Exception e) {
			logger.warn("REDIS 操作用户短信配置失败", e);
		}
	}

	public boolean reloadToRedis() {
		List<UserSmsConfig> list = this.list();
		if (CollUtil.isEmpty(list)) {
			logger.error("用户短信配置数据为空");
			return true;
		}

		List<Object> con = stringRedisTemplate.execute((connection) -> {

			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (UserSmsConfig config : list) {
				byte[] key = serializer.serialize(getKey(config.getUserId()));
				connection.set(key, serializer.serialize(JSON.toJSONString(config)));
			}

			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

}