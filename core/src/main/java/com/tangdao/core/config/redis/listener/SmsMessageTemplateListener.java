package com.tangdao.core.config.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.model.domain.SmsMessageTemplate;
import com.tangdao.core.service.SmsMessageTemplateService;

/**
 * 
 * <p>
 * TODO 短信模板广播监听
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class SmsMessageTemplateListener extends MessageListenerAdapter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			if (message == null) {
				return;
			}

			SmsMessageTemplate template = JSON.parseObject(message.toString(), SmsMessageTemplate.class);
			if (template == null) {
				return;
			}

			logger.info("订阅短信模板数据[" + message.toString() + "]，将做清除处理");

			// 清空后，采用延期加载方式填充数据，即使用的时候才会去REDIS查询并填充
			SmsMessageTemplateService.GLOBAL_MESSAGE_TEMPLATE.remove(template.getUserId());

		} catch (Exception e) {
			logger.warn("短信模板订阅删除数据失败", e);
		}
	}
}
