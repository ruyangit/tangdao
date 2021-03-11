package com.tangdao.core.config.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.context.SettingsContext.MessageAction;
import com.tangdao.core.service.SmsMobileBlacklistService;

/**
 * 
 * <p>
 * TODO 黑名单手机广播监听
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class SmsMobileBlacklistListener extends MessageListenerAdapter {

	public SmsMobileBlacklistListener(StringRedisTemplate stringRedisTemplate) {
		super();
		this.stringRedisTemplate = stringRedisTemplate;
	}

	private StringRedisTemplate stringRedisTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			if (message == null) {
				return;
			}

			String[] report = message.toString().split(":");
			if (report.length == 0) {
				logger.warn("黑名单订阅数据失败");
				return;
			}

			MessageAction action = MessageAction.parse(Integer.parseInt(report[0]));
			if (MessageAction.ADD == action) {
				SmsMobileBlacklistService.GLOBAL_MOBILE_BLACKLIST.put(report[1], Integer.parseInt(report[2]));
				stringRedisTemplate.opsForHash().put(SmsRedisConstant.RED_MOBILE_BLACKLIST, report[1], report[2]);
			} else if (MessageAction.REMOVE == action) {
				SmsMobileBlacklistService.GLOBAL_MOBILE_BLACKLIST.remove(report[1]);
				stringRedisTemplate.opsForHash().delete(SmsRedisConstant.RED_MOBILE_BLACKLIST, report[1]);
			}

			logger.info("黑名单订阅数据：{} 成功", JSON.toJSONString(report));

		} catch (Exception e) {
			logger.warn("黑名单订阅数据失败", e);
		}
	}
}
