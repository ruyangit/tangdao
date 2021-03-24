package com.tangdao.core.config.rabbit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;
import com.tangdao.core.config.runner.AbstractInitializeRunner;

public abstract class AbstartRabbitListener implements ChannelAwareMessageListener {
	
	/**
	 * 日志服务
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 检验是否可以消费，主要是判断资源数据是否都已经初始化完毕
	 */
	protected void checkIsStartingConsumeMessage() {
		if (!AbstractInitializeRunner.isResourceInitFinished) {
			AbstractInitializeRunner.LOCK.lock();
			try {
				AbstractInitializeRunner.CONDITION.await();
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			} finally {
				AbstractInitializeRunner.LOCK.unlock();
			}
		}
	}

	/**
	 * 消费MQ
	 * 
	 * @param message
	 * @param channel
	 * @throws Exception com.rabbitmq.client.Channel)
	 */
	public abstract void onMessage(Message message, Channel channel) throws Exception;
}
