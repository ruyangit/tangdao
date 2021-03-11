/**
 *
 */
package com.tangdao.exchanger.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.tangdao.core.config.redis.listener.SmsMessageTemplateListener;
import com.tangdao.core.config.redis.listener.SmsMobileBlacklistListener;
import com.tangdao.core.config.redis.listener.SmsPassageAccessListener;
import com.tangdao.core.constant.SmsRedisConstant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Configuration
public class RedisConfig {

	/**
	 * 黑名单数据变更广播通知监听配置
	 * 
	 * @return 消息监听适配器
	 */
	@Bean
	MessageListenerAdapter smsMobileBlacklistMessageListener(StringRedisTemplate stringRedisTemplate) {
		return new SmsMobileBlacklistListener(stringRedisTemplate);
	}

	/**
	 * 短信模板变更广播通知监听配置
	 * 
	 * @return 短信模板监听器
	 */
	@Bean
	MessageListenerAdapter smsMessageTemplateMessageListener() {
		return new SmsMessageTemplateListener();
	}

	/**
	 * 可用通道变更广播通知监听配置
	 * 
	 * @return 可用通道监听器
	 */
	@Bean
	MessageListenerAdapter smsPassageAccessMessageListener() {
		return new SmsPassageAccessListener();
	}

	@Bean
	RedisMessageListenerContainer redisContainer(StringRedisTemplate stringRedisTemplate,
			JedisConnectionFactory jedisConnectionFactory) {
		final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(jedisConnectionFactory);
		container.addMessageListener(smsMobileBlacklistMessageListener(stringRedisTemplate), mobileBlacklistTopic());
		container.addMessageListener(smsMessageTemplateMessageListener(), messageTemplateTopic());
		container.addMessageListener(smsPassageAccessMessageListener(), passageAccessTopic());
		return container;
	}

	@Bean
	Topic mobileBlacklistTopic() {
		return new PatternTopic(SmsRedisConstant.BROADCAST_MOBILE_BLACKLIST_TOPIC);
	}

	@Bean
	Topic messageTemplateTopic() {
		return new PatternTopic(SmsRedisConstant.BROADCAST_MESSAGE_TEMPLATE_TOPIC);
	}

	@Bean
	Topic passageAccessTopic() {
		return new PatternTopic(SmsRedisConstant.BROADCAST_PASSAGE_ACCESS_TOPIC);
	}
}
