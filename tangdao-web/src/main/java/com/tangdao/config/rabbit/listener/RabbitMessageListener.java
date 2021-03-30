package com.tangdao.config.rabbit.listener;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.tangdao.config.rabbit.AbstartRabbitListener;
import com.tangdao.core.constant.RabbitConstant;

/**
 * 
 * <p>
 * TODO 测试消息
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class RabbitMessageListener extends AbstartRabbitListener {

	@Autowired
	private MessageConverter messageConverter;

	@Override
	@RabbitListener(queues = RabbitConstant.MQ_MESSAGE)
	public void onMessage(Message message, Channel channel) throws Exception {

		checkIsStartingConsumeMessage();

		try {
			messageConverter.fromMessage(message);
		} catch (Exception e) {
			// 需要做重试判断
			logger.error("MQ消费网关状态回执数据失败： {}", messageConverter.fromMessage(message), e);
		} finally {
			// 确认消息成功消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

}
