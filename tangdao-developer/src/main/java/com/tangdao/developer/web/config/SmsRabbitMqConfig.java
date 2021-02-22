/**
 *
 */
package com.tangdao.developer.web.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@Configuration
public class SmsRabbitMqConfig {

	@Value("${mq.rabbit.host}")
	private String mqHost;

	@Value("${mq.rabbit.port}")
	private Integer mqPort;

	@Value("${mq.rabbit.username}")
	private String mqUsername;

	@Value("${mq.rabbit.password}")
	private String mqPassword;

	@Value("${mq.rabbit.vhost.sms}")
	private String mqVhost;

	@Bean("smsConnectionFactory")
	@Primary
	public ConnectionFactory smsConnectionFactory() {
		CachingConnectionFactory smsConnectionFactory = new CachingConnectionFactory();
		smsConnectionFactory.setHost(mqHost);
		smsConnectionFactory.setPort(mqPort);
		smsConnectionFactory.setUsername(mqUsername);
		smsConnectionFactory.setPassword(mqPassword);
		smsConnectionFactory.setVirtualHost(mqVhost);
		smsConnectionFactory.setPublisherReturns(true);

		// 显性设置后才能进行回调函数设置 enable confirm mode
		// confirm 是为了保障数据发送到队列的必答性（对于发送者）
		smsConnectionFactory.setPublisherReturns(true);
		smsConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

		// 默认 connectionFactory.setCacheMode(CacheMode.CHANNEL), ConnectionCacheSize无法设置
		smsConnectionFactory.setChannelCacheSize(100);

		smsConnectionFactory.setRequestedHeartBeat(60);

		// 设置超时时间15秒
		smsConnectionFactory.setConnectionTimeout(15000);

		// 断开重连接，保证数据无丢失,默认为true
//		smsConnectionFactory.setAutomaticRecoveryEnabled(true);

		return smsConnectionFactory;
	}

	@Bean("smsRabbitTemplate")
	public RabbitTemplate smsRabbitTemplate(@Qualifier("smsConnectionFactory") ConnectionFactory smsConnectionFactory,
			Jackson2JsonMessageConverter messageConverter, RetryTemplate retryTemplate) {
		RabbitTemplate smsRabbitTemplate = new RabbitTemplate(smsConnectionFactory);
		smsRabbitTemplate.setMessageConverter(messageConverter);
		smsRabbitTemplate.setRetryTemplate(retryTemplate);
		return smsRabbitTemplate;
	}
}
