package com.tangdao.core.config.redis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * 
 * <p>
 * TODO 广播监听
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class MessageTemplateListener extends MessageListenerAdapter {

	@Override
	public void onMessage(Message message, byte[] pattern) {

	}
}
