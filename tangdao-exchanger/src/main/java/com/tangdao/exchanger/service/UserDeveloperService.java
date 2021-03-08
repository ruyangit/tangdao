package com.tangdao.exchanger.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.CommonRedisConstant;
import com.tangdao.core.model.domain.paas.UserDeveloper;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.UserDeveloperMapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;

@Service
public class UserDeveloperService extends BaseService<UserDeveloperMapper, UserDeveloper> {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private String getKey(String appkey) {
		return String.format("%s:%s", CommonRedisConstant.RED_DEVELOPER_LIST, appkey);
	}

	public UserDeveloper getByAppkey(String appkey) {
		// TODO Auto-generated method stub
		try {
			Object d = stringRedisTemplate.opsForValue().get(getKey(appkey));
			if (d != null) {
				return JSON.parseObject(d.toString(), UserDeveloper.class);
			}
		} catch (Exception e) {
			logger.error("REDIS获取开发者数据失败, appkey: {}", appkey, e);
		}
		return this.getOne(Wrappers.<UserDeveloper>lambdaQuery().eq(UserDeveloper::getAppKey, appkey));
	}

	public UserDeveloper getByAppkey(String appkey, String appSecret) {
		// TODO Auto-generated method stub
		if (StrUtil.isEmpty(appkey) || StrUtil.isEmpty(appSecret)) {
			return null;
		}
		return this.getOne(Wrappers.<UserDeveloper>lambdaQuery().eq(UserDeveloper::getAppKey, appkey)
				.eq(UserDeveloper::getAppSecret, appSecret));
	}

	public UserDeveloper saveWithReturn(String userId, String appKey) {
		UserDeveloper developer = new UserDeveloper();
		developer.setUserId(userId);
		developer.setAppKey(appKey);
		developer.setSalt(BCrypt.gensalt());
		developer.setAppSecret(BCrypt.hashpw(appKey, developer.getSalt()));
		if (this.save(developer)) {
			pushToRedis(developer);
			return developer;
		}
		return null;
	}

	public boolean reloadToRedis() {
		List<UserDeveloper> list = this.list();
		if (CollUtil.isEmpty(list)) {
			log.warn("可用开发者数据为空");
			return false;
		}
		List<Object> con = stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			for (UserDeveloper developer : list) {
				byte[] key = serializer.serialize(getKey(developer.getAppKey()));
				connection.set(key, serializer.serialize(JSON.toJSONString(developer)));
			}
			return connection.closePipeline();

		}, false, true);

		return CollUtil.isNotEmpty(con);
	}

	private boolean pushToRedis(UserDeveloper developer) {
		try {
			stringRedisTemplate.opsForValue().set(getKey(developer.getAppKey()), JSON.toJSONString(developer));
			return true;
		} catch (Exception e) {
			logger.warn("REDIS 加载用户映射数据失败", e);
			return false;
		}
	}

	public boolean update(UserDeveloper developer) {
		// developer secret not null,update developer secret and salt
		UserDeveloper d = this.getById(developer);
		// 2017-02-27 判断加密串是否有变化，如果
		if (StrUtil.isNotEmpty(developer.getAppSecret()) && !developer.getAppSecret().equals(d.getAppSecret())) {
			d.setSalt(developer.getSalt());
			d.setAppSecret(developer.getAppSecret());
		}

		// 判断APPKEY是否发生变化
		if (StrUtil.isNotEmpty(developer.getAppKey()) && !developer.getAppKey().equals(d.getAppKey())) {
			// 如果APPKEY 发生变化，则需要移除原有的缓存，产生新的缓存信息
			removeRedis(d.getAppKey());
			d.setAppKey(developer.getAppKey());
		}

		boolean result = this.update(d);
		if (result) {
			pushToRedis(d);
		}

		return result;
	}

	private boolean removeRedis(String appkey) {
		try {
			stringRedisTemplate.delete(getKey(appkey));
			return true;
		} catch (Exception e) {
			logger.warn("REDIS 移除用户映射数据失败, appkey : {}", appkey, e);
			return false;
		}
	}

}
