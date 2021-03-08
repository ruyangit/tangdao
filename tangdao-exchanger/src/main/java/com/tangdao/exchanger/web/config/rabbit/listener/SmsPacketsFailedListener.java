package com.tangdao.exchanger.web.config.rabbit.listener;

import java.util.ArrayList;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;

import com.rabbitmq.client.Channel;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.exchanger.web.config.rabbit.AbstartRabbitListener;

/**
 * 
 * <p>
 * TODO 针对提交网关失败或者黑名单等逻辑错误进行伪造包处理
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Component
public class SmsPacketsFailedListener extends AbstartRabbitListener {

	@Autowired
	private SmsMtSubmitService smsMtSubmitService;

	@Autowired
	private Jackson2JsonMessageConverter messageConverter;

	@Override
	@RabbitListener(queues = RabbitConstant.MQ_SMS_MT_PACKETS_EXCEPTION)
	public void onMessage(Message message, Channel channel) throws Exception {

		checkIsStartingConsumeMessage();

		try {
			SmsMtMessageSubmit submit = (SmsMtMessageSubmit) messageConverter.fromMessage(message);

			List<SmsMtMessageSubmit> submits = new ArrayList<>();
			submits.add(submit);

			smsMtSubmitService.doSmsException(submits);

		} catch (Exception e) {
			logger.error("MQ消费分包失败数据异常： {}", messageConverter.fromMessage(message), e);
		} finally {
			// 确认消息成功消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

}
