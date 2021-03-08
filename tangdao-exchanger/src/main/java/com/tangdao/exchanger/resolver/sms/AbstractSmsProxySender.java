package com.tangdao.exchanger.resolver.sms;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.tangdao.core.model.domain.sms.PassageParameter;
import com.tangdao.exchanger.service.SmsProxyManager;

/**
 * TODO 直连抽象类
 * 
 * @author ruyang
 * @version V1.0
 * @date 2018年8月5日 上午12:21:38
 */
public abstract class AbstractSmsProxySender {

	@Autowired
	protected SmsProxyManager smsProxyService;

	@Resource
	protected RabbitTemplate rabbitTemplate;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 通道ID锁
	 */
	private final Map<String, Object> passageLockMonitor = new ConcurrentHashMap<>();

	private void addPassageLockMonitor(String passageId) {
		passageLockMonitor.putIfAbsent(passageId, new Object());
	}

	/**
	 * TODO 获取直连对象的代理信息（不同通道间初始化互不影响）
	 * 
	 * @param parameter
	 * @return
	 */
	protected Object getSmManageProxy(PassageParameter parameter) {
		addPassageLockMonitor(parameter.getPassageId());

		synchronized (passageLockMonitor.get(parameter.getPassageId())) {
			if (smsProxyService.isProxyAvaiable(parameter.getPassageId())) {
				return SmsProxyManager.getManageProxy(parameter.getPassageId());
			}

			boolean isOk = smsProxyService.startProxy(parameter);
			if (!isOk) {
				return null;
			}

			// 重新初始化后将错误计数器归零
			smsProxyService.clearSendErrorTimes(parameter.getPassageId());

			return SmsProxyManager.GLOBAL_PROXIES.get(parameter.getPassageId());
		}
	}

	/**
	 * TODO 断开直连协议连接
	 * 
	 * @param passageId
	 */
	public void onTerminate(String passageId) {
		smsProxyService.stopProxy(passageId);
	}
}
