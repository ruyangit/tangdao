package com.tangdao.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.constant.RedisConstant;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Service
@EnableAsync
public class AsyncService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Async
	public void publishMobilesLocalToRedis() {
		long startTime = System.currentTimeMillis();

		stringRedisTemplate.execute((connection) -> {
			RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
			connection.openPipeline();
			byte[] key = serializer.serialize(RedisConstant.RED_AREA_MOBILES_LOCAL);

			byte[] value = JSON.toJSONBytes(RedisConstant.GLOBAL_MOBILES_LOCAL);

			connection.set(key, value);

			return connection.closePipeline();

		}, false, true);

		logger.info("Mobiles area local data has published to redis, it costs {} ms",
				(System.currentTimeMillis() - startTime));
	}

}
