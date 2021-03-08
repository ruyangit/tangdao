package com.tangdao.exchanger.service;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.constant.CommonRedisConstant;

@Service
@EnableAsync
public class AsyncService {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Async
	public void publishMobilesLocalToRedis() {
		long startTime = System.currentTimeMillis();

		stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			byte[] key = serializer.serialize(CommonRedisConstant.RED_AREA_MOBILES_LOCAL);

			byte[] value = JSON.toJSONBytes(CommonRedisConstant.GLOBAL_MOBILES_LOCAL);

			connection.set(key, value);

			return connection.closePipeline();

		}, false, true);

		logger.info("Mobiles area local data has published to redis, it costs {} ms",
				(System.currentTimeMillis() - startTime));
	}

}
