/**
 * 
 */
package com.tangdao.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.tangdao.core.config.redis.serializer.RedisObjectSerializer;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <p>
 * TODO 缓存配置
 * </p>
 *
 * @author ruyang
 * @since 2020年10月20日
 */
@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${redis.host:localhost}")
	private String host;
	@Value("${redis.port:6379}")
	private int port;
	@Value("${redis.password:}")
	private String password;
	@Value("${redis.database:0}")
	private int database;
	@Value("${redis.timeout:30000}")
	private Long timeout;

	@Value("${redis.cluster.enabled:false}")
	private boolean clusterEnabled;
	@Value("${redis.cluster.nodes:}")
	private List<String> nodes;
	@Value("${redis.cluster.max-redirects:3}")
	private Integer maxRedirects;

	@Value("${redis.jedis.pool.max-active:64}")
	private Integer maxActive;
	@Value("${redis.jedis.pool.min-idle:2}")
	private Integer minIdle;
	@Value("${redis.jedis.pool.max-idle:8}")
	private Integer maxIdle;
	@Value("${redis.jedis.pool.max-wait:3000}")
	private Integer maxWait;

	/**
	 * 默认10分钟
	 */
	@Value("${redis.region.default:600}")
	private Long regionDefault;

	@Value("${redis.region.cachenames:}")
	public List<String> regionCachenames;

	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxActive);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWait);
		return jedisPoolConfig;
	}

	@Bean
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setDatabase(database);
		redisStandaloneConfiguration.setPassword(password);
		return redisStandaloneConfiguration;
	}

	@Bean
	public RedisClusterConfiguration redisClusterConfiguration() {
		Set<String> clusterNodes = new HashSet<String>();
		for (String nd : nodes) {
			if (StrUtil.isNotEmpty(nd)) {
				clusterNodes.add(StrUtil.trim(nd));
			}
		}
		RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterNodes);
		redisClusterConfiguration.setPassword(password);
		redisClusterConfiguration.setMaxRedirects(maxRedirects);
		return redisClusterConfiguration;
	}

	@Primary
	@Bean(name = "jedisConnectionFactory")
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
				.builder();
		jedisClientConfiguration.connectTimeout(Duration.ofMillis(timeout));
		if (clusterEnabled) {
			return new JedisConnectionFactory(redisClusterConfiguration(), jedisClientConfiguration.build());
		} else {
			return new JedisConnectionFactory(redisStandaloneConfiguration(), jedisClientConfiguration.build());
		}
	}

	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(JedisConnectionFactory jedisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(jedisConnectionFactory);

		// key采用String的序列化方式
		redisTemplate.setKeySerializer(keySerializer());
		// hash的key也采用String的序列化方式
		redisTemplate.setHashKeySerializer(keySerializer());
		// value序列化方式采用jackson
		redisTemplate.setValueSerializer(valueSerializer());
		// hash的value序列化方式采用jackson
		redisTemplate.setHashValueSerializer(valueSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(
			@Qualifier("jedisConnectionFactory") RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		redisTemplate.setKeySerializer(keySerializer());
		// hash的key也采用String的序列化方式
		redisTemplate.setHashKeySerializer(keySerializer());
		// value序列化方式采用jackson
		redisTemplate.setValueSerializer(valueSerializer());
		// hash的value序列化方式采用jackson
		redisTemplate.setHashValueSerializer(valueSerializer());
		redisTemplate.afterPropertiesSet();
		return redisTemplate;
	}

	public StringRedisSerializer keySerializer() {
		return new StringRedisSerializer();
	}

	public RedisObjectSerializer valueSerializer() {
		return new RedisObjectSerializer();
	}

	/**
	 * 自定义监听
	 * 
	 * @param stringRedisTemplate
	 * @param jedisConnectionFactory
	 * @return
	 */
	@Bean
	RedisMessageListenerContainer redisContainer(StringRedisTemplate stringRedisTemplate,
			JedisConnectionFactory jedisConnectionFactory) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory);
		return container;
	}

	@Bean
	public KeyGenerator wiselyKeyGenerator() {
		return (target, method, params) -> {
			StringBuilder sb = new StringBuilder();
			sb.append(target.getClass().getName());
			sb.append(method.getName());
			for (Object obj : params) {
				sb.append(obj.toString());
			}
			return sb.toString();

		};
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		log.debug("redis cache-manager default:{}", this.regionDefault);
		RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
				.cacheDefaults(getRedisCacheConfigurationWithTtl(this.regionDefault))
				.withInitialCacheConfigurations(getRedisCacheConfigurationMap()).transactionAware().build();

		return redisCacheManager;
	}

	/**
	 * 
	 * TODO 自定义缓存配置
	 * 
	 * @return
	 */
	private Map<String, RedisCacheConfiguration> getRedisCacheConfigurationMap() {
		Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
		if (CollUtil.isNotEmpty(regionCachenames)) {
			regionCachenames.stream().forEach(value -> {
				if (value != null) {
					int index = value.lastIndexOf("::");
					if (index > -1) {
						String cacheName = value.substring(0, index);
						Long withTtl = Long.parseLong(value.substring(index + 2, value.length()));
						log.debug("redis custom cachename: {}::{}", cacheName, withTtl);
						redisCacheConfigurationMap.put(cacheName, this.getRedisCacheConfigurationWithTtl(withTtl));
					}
				}
			});
		}
		return redisCacheConfigurationMap;
	}

	/**
	 * 
	 * TODO 設置缓存时间
	 * 
	 * @param second 秒
	 * @return
	 */
	private RedisCacheConfiguration getRedisCacheConfigurationWithTtl(Long second) {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(second)) // *s 缓存失效
				// 设置key的序列化方式
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
				// 设置value的序列化方式
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
				// 不缓存null值
				.disableCachingNullValues();

	}

}
