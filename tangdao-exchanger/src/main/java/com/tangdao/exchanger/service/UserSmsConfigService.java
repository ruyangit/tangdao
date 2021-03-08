package com.tangdao.exchanger.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.CommonRedisConstant;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.model.domain.paas.UserSmsConfig;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.UserSmsConfigMapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserSmsConfigService extends BaseService<UserSmsConfigMapper, UserSmsConfig> {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private static String getKey(String userCode) {
		return CommonRedisConstant.RED_USER_SMS_CONFIG + ":" + userCode;
	}

	public UserSmsConfig getByAppId(String appId) {
		if (StrUtil.isEmpty(appId)) {
			return null;
		}

		try {
			String text = stringRedisTemplate.opsForValue().get(getKey(appId));
			if (StrUtil.isNotEmpty(text)) {
				return JSON.parseObject(text, UserSmsConfig.class);
			}

		} catch (Exception e) {
			log.warn("REDIS获取用户短信配置失败", e);
		}

		return this.getOne(Wrappers.<UserSmsConfig>lambdaQuery().eq(UserSmsConfig::getAppId, appId));
	}

	public int getSingleChars(String appId) {
		int wordsPerNum = UserBalanceConstant.WORDS_SIZE_PER_NUM;
		try {
			UserSmsConfig userSmsConfig = getByAppId(appId);
			if (userSmsConfig != null) {
				wordsPerNum = userSmsConfig.getSmsWords();
			}

		} catch (Exception e) {
			log.warn("appId：{} 短信字数配置失败，将以默认每条字数：{}计费", appId, wordsPerNum, e);
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

	public boolean save(String appId, int words, String extNumber) {
		UserSmsConfig userSmsConfig = defaultConfig();
		userSmsConfig.setAppId(appId);
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
			stringRedisTemplate.opsForValue().set(getKey(userSmsConfig.getAppId()), JSON.toJSONString(userSmsConfig));
		} catch (Exception e) {
			log.warn("REDIS 操作用户短信配置失败", e);
		}
	}

	public boolean reloadToRedis() {
		List<UserSmsConfig> list = this.list();
		if (CollUtil.isEmpty(list)) {
			log.error("用户短信配置数据为空");
			return true;
		}

		List<Object> con = stringRedisTemplate.execute((connection) -> {

			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (UserSmsConfig config : list) {
				byte[] key = serializer.serialize(getKey(config.getAppId()));
				connection.set(key, serializer.serialize(JSON.toJSONString(config)));
			}

			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

}