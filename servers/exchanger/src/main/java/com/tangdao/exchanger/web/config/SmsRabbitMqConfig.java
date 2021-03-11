/**
 * 
 */
package com.tangdao.exchanger.web.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.tangdao.core.constant.RabbitConstant;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Configuration
@EnableRabbit
@Order(4)
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

	/**
	 * 初始化消费者数量
	 */
	@Value("${mq.rabbit.consumers}")
	private int concurrentConsumers;

	/**
	 * 消费者最大数量（当队列堆积过量，则会自动增加消费者，直至达到最大）
	 */
	@Value("${mq.rabbit.maxconsumers}")
	private int maxConcurrentConsumers;

	/**
	 * 每次队列中取数据个数，如果为1则保证消费者间平均消费
	 */
	@Value("${mq.rabbit.prefetch:1}")
	private int rabbitPrefetchCount;

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
//		smsConnectionFactory.setPublisherConfirms(true);
		smsConnectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);

		// 默认 connectionFactory.setCacheMode(CacheMode.CHANNEL), ConnectionCacheSize无法设置
		smsConnectionFactory.setChannelCacheSize(100);
//		connectionFactory.setConnectionCacheSize(5);

		smsConnectionFactory.setRequestedHeartBeat(60);

		// 设置超时时间15秒
		smsConnectionFactory.setConnectionTimeout(15000);

		// 断开重连接，保证数据无丢失,默认为true
//		smsConnectionFactory.setAutomaticRecoveryEnabled(true);

		return smsConnectionFactory;
	}

	@Bean
	public RabbitAdmin rabbitAdmin(@Qualifier("smsConnectionFactory") ConnectionFactory smsConnectionFactory) {
		RabbitAdmin rabbitAdmin = new RabbitAdmin(smsConnectionFactory);
		DirectExchange exchange = new DirectExchange(RabbitConstant.EXCHANGE_SMS, true, false);
		rabbitAdmin.declareExchange(exchange);
		rabbitAdmin.setAutoStartup(true);

		// 待分包处理队列
		Queue smsWaitProcessQueue = new Queue(RabbitConstant.MQ_SMS_MT_WAIT_PROCESS, true, false, false,
				setQueueFeatures());
		Binding smsWaitProcessBinding = BindingBuilder.bind(smsWaitProcessQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MT_WAIT_PROCESS);
		rabbitAdmin.declareQueue(smsWaitProcessQueue);
		rabbitAdmin.declareBinding(smsWaitProcessBinding);

		// 待点对点短信分包处理队列
		Queue smsP2PWaitProcessQueue = new Queue(RabbitConstant.MQ_SMS_MT_P2P_WAIT_PROCESS, true, false, false,
				setQueueFeatures());
		Binding smsP2PWaitProcessBinding = BindingBuilder.bind(smsP2PWaitProcessQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MT_P2P_WAIT_PROCESS);
		rabbitAdmin.declareQueue(smsP2PWaitProcessQueue);
		rabbitAdmin.declareBinding(smsP2PWaitProcessBinding);

		// 分包处理完成，待提交网关（上家通道）队列，因每个队列需要不同的消费者，所以动态控制@SmsWaitSubmitListener
//		Queue smsWaitSubmitQueue = new Queue(RabbitConstant.MQ_SMS_MT_WAIT_SUBMIT, true, false, false, setQueueFeatures());
//		Binding smsWaitSubmitBinding = BindingBuilder.bind(smsWaitSubmitQueue).to(exchange)
//				.with(RabbitConstant.MQ_SMS_MT_WAIT_SUBMIT);
//		rabbitAdmin.declareQueue(smsWaitSubmitQueue);
//		rabbitAdmin.declareBinding(smsWaitSubmitBinding);

		// 网关回执数据，带解析回执数据并推送
		Queue smsWaitReceiptQueue = new Queue(RabbitConstant.MQ_SMS_MT_WAIT_RECEIPT, true, false, false,
				setQueueFeatures());
		Binding smsWaitReceiptBinding = BindingBuilder.bind(smsWaitReceiptQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MT_WAIT_RECEIPT);
		rabbitAdmin.declareQueue(smsWaitReceiptQueue);
		rabbitAdmin.declareBinding(smsWaitReceiptBinding);

		// 用户上行回复记录
		Queue moReceiveQueue = new Queue(RabbitConstant.MQ_SMS_MO_RECEIVE, true, false, false, setQueueFeatures());
		Binding moReceiveBinding = BindingBuilder.bind(moReceiveQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MO_RECEIVE);
		rabbitAdmin.declareQueue(moReceiveQueue);
		rabbitAdmin.declareBinding(moReceiveBinding);

		// 用户推送数据，带解析推送数据并推送(上行)
		Queue moWaitPushQueue = new Queue(RabbitConstant.MQ_SMS_MO_WAIT_PUSH, true, false, false, setQueueFeatures());
		Binding moWaitPushBinding = BindingBuilder.bind(moWaitPushQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MO_WAIT_PUSH);
		rabbitAdmin.declareQueue(moWaitPushQueue);
		rabbitAdmin.declareBinding(moWaitPushBinding);

		// 下行分包异常，如黑名单数据
		Queue packetsExceptionQueue = new Queue(RabbitConstant.MQ_SMS_MT_PACKETS_EXCEPTION, true, false, false,
				setQueueFeatures());
		Binding packetsExceptionBinding = BindingBuilder.bind(packetsExceptionQueue).to(exchange)
				.with(RabbitConstant.MQ_SMS_MT_PACKETS_EXCEPTION);
		rabbitAdmin.declareQueue(packetsExceptionQueue);
		rabbitAdmin.declareBinding(packetsExceptionBinding);

		return rabbitAdmin;
	}

	@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	@Bean("smsRabbitTemplate")
	RabbitTemplate smsRabbitTemplate(@Qualifier("smsConnectionFactory") ConnectionFactory smsConnectionFactory,
			Jackson2JsonMessageConverter messageConverter) {
		RabbitTemplate smsRabbitTemplate = new RabbitTemplate(smsConnectionFactory);

		smsRabbitTemplate.setMessageConverter(messageConverter);

		setRetryTemplate(smsRabbitTemplate);

		return smsRabbitTemplate;
	}

	@Bean
	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			@Qualifier("smsConnectionFactory") ConnectionFactory smsConnectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(smsConnectionFactory);

		factory.setConcurrentConsumers(concurrentConsumers);
		factory.setMaxConcurrentConsumers(maxConcurrentConsumers);
		// 消息失败重试后 设置 RabbitMQ把消息分发给有空的cumsuer，同 channel.basicQos(1);
		factory.setPrefetchCount(rabbitPrefetchCount);
//		factory.setConsumerTagStrategy(consumerTagStrategy);

		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        factory.setMessageConverter(messageConverter());

//		factory.setMissingQueuesFatal(missingQueuesFatal);

//		ExecutorService service = Executors.newFixedThreadPool(500);
//		factory.setTaskExecutor(service);

		factory.setAutoStartup(true);

		return factory;
	}

	/**
	 * TODO 设置重试模板
	 * 
	 * @param rabbitTemplate
	 */
	private void setRetryTemplate(RabbitTemplate rabbitTemplate) {
		// 重试模板
		RetryTemplate retryTemplate = new RetryTemplate();
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(500);
		backOffPolicy.setMultiplier(10.0);
		backOffPolicy.setMaxInterval(10000);
		retryTemplate.setBackOffPolicy(backOffPolicy);
		rabbitTemplate.setRetryTemplate(retryTemplate);
	}

	/**
	 * TODO 设置队列属性
	 */
	private Map<String, Object> setQueueFeatures() {
		Map<String, Object> args = new HashMap<>();
		// 最大优先级定义
		args.put("x-max-priority", 10);
		// 过期时间，单位毫秒
//		args .put("x-message-ttl", 60000);
		// 30分钟，单位毫秒
//		args.put("x-expires", 1800000); 
		// 集群高可用，数据复制
		args.put("x-ha-policy", "all");
		return args;
	}
}
