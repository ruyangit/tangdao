package com.tangdao.core.config.redis.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.constant.SmsRedisConstant;
import com.tangdao.core.model.domain.SmsPassageAccess;
import com.tangdao.core.service.SmsPassageAccessService;

/**
 * 可用通道广播监听
 * 
 * @author ruyang
 * @version V1.0
 * @date 2018年6月9日 下午3:04:21
 */
public class SmsPassageAccessListener extends MessageListenerAdapter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			if (message == null) {
				return;
			}

			SmsPassageAccess access = JSON.parseObject(message.toString(), SmsPassageAccess.class);
			if (access == null) {
				return;
			}

			logger.info("订阅可用通道数据[" + message.toString() + "]，将做清除处理");

			// 清空后，采用延期加载方式填充数据，即使用的时候才会去REDIS查询并填充
			SmsPassageAccessService.GLOBAL_PASSAGE_ACCESS_CONTAINER.remove(getKey(access));

		} catch (Exception e) {
			logger.warn("可用通道订阅数据失败", e);
		}
	}

	private String getKey(SmsPassageAccess access) {
		return getMainKey(access) + SmsPassageAccessService.MAP_KEY_SEPERATOR + getAssistKey(access);
	}

	private String getAssistKey(SmsPassageAccess access) {
		return String.format("%d:%d:%d", access.getRouteType(), access.getCmcp(), access.getAreaCode());
	}

	/**
	 * 获取REDIS主KEY
	 *
	 * @param access 可用通道数据
	 * @return KEY
	 */
	private String getMainKey(SmsPassageAccess access) {
		return String.format("%s:%d:%d", SmsRedisConstant.RED_USER_PASSAGE_ACCESS, access.getUserId(),
				access.getCallType());
	}
}
