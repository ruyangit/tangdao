/**
 *
 */
package com.tangdao.exchanger.service;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import com.huawei.insa2.util.Args;
import com.tangdao.core.context.CommonContext.ProtocolType;
import com.tangdao.core.exception.DataEmptyException;
import com.tangdao.core.model.domain.sms.PassageParameter;
import com.tangdao.exchanger.resolver.sms.cmpp.v2.CmppManageProxy;
import com.tangdao.exchanger.resolver.sms.cmpp.v2.CmppProxySender;
import com.tangdao.exchanger.resolver.sms.cmpp.v3.Cmpp3ManageProxy;
import com.tangdao.exchanger.resolver.sms.cmpp.v3.Cmpp3ProxySender;
import com.tangdao.exchanger.resolver.sms.sgip.SgipManageProxy;
import com.tangdao.exchanger.resolver.sms.sgip.SgipProxySender;
import com.tangdao.exchanger.resolver.sms.sgip.constant.SgipConstant;
import com.tangdao.exchanger.resolver.sms.smgp.SmgpManageProxy;
import com.tangdao.exchanger.resolver.sms.smgp.SmgpProxySender;
import com.tangdao.exchanger.template.TParameter;
import com.tangdao.exchanger.template.handler.RequestTemplateHandler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月25日
 */
@Service
public class SmsProxyManager {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CmppProxySender cmppProxySender;

	@Autowired
	private Cmpp3ProxySender cmpp3ProxySender;

	@Autowired
	private SgipProxySender sgipProxySender;

	@Autowired
	private SmgpProxySender smgpProxySender;

	/**
	 * CMPP/SGIP/SMGP通道代理发送实例
	 */
	public static volatile Map<String, Object> GLOBAL_PROXIES = new ConcurrentHashMap<>();

	/**
	 * 通道PROXY 发送错误次数计数器
	 */
	private static final Map<String, Integer> GLOBAL_PROXIES_ERROR_COUNTER = new HashMap<>();

	/**
	 * 通道对应的限流器容器
	 */
	public static final Map<String, RateLimiter> GLOBAL_RATE_LIMITERS = new HashMap<>();

	/**
	 * 默认限流速度
	 */
	public static final int DEFAULT_LIMIT_SPEED = 500;

	/**
	 * 联通重连超时时间
	 */
	private static final int SGIP_RECONNECT_TIMEOUT = 60 * 1000;

	public boolean startProxy(PassageParameter parameter) {
		try {
			if (parameter == null) {
				throw new IllegalArgumentException("SmsPassageParameter is empty");
			}

			TParameter tparameter = RequestTemplateHandler.parse(parameter.getParams());
			if (CollUtil.isEmpty(tparameter)) {
				throw new IllegalArgumentException("SmsPassageParameter's params are empty");
			}

			return setupPassageProxyIfNecessary(getPassageProtocolType(parameter), parameter.getPassageId(), tparameter,
					parameter.getPacketsSize());
		} catch (Exception e) {
			logger.error("Starting passage's proxy failed, args[" + JSON.toJSONString(parameter) + "]", e);
			return false;
		}
	}

	/**
	 * TODO 获取通道协议类型
	 * 
	 * @param parameter
	 * @return
	 */
	private ProtocolType getPassageProtocolType(PassageParameter parameter) {
		if (StrUtil.isEmpty(parameter.getProtocol())) {
			throw new IllegalArgumentException("SmsPassageParameter's protocolType is empty");
		}

		ProtocolType protocolType = ProtocolType.parse(parameter.getProtocol());
		if (protocolType == null) {
			throw new IllegalArgumentException(
					"SmsPassageParameter's protocolType[" + parameter.getProtocol() + "] is not matched");
		}

		return protocolType;
	}

	/**
	 * TODO 初始化CMPP通道
	 * 
	 * @param passageId
	 * @param tparameter
	 * @param speed      限速
	 * @return
	 */
	private boolean setupPassageProxyIfNecessary(ProtocolType protocolType, String passageId, TParameter tparameter,
			Integer speed) {
		// 如果协议非直连协议（如HTTP，WebService等）
		if (!ProtocolType.isBelongtoDirect(protocolType.name())) {
			return true;
		}

		boolean isLoadSucceed = loadManageProxy(passageId, tparameter, protocolType);
		if (!isLoadSucceed) {
			return false;
		}

		bindPassageRateLimiter(passageId, speed);

		return true;
	}

	/**
	 * TODO 加载限速控制器
	 * 
	 * @param passageId
	 * @param speed
	 */
	private void bindPassageRateLimiter(String passageId, Integer speed) {
		RateLimiter limiter = RateLimiter.create((speed == null || speed == 0) ? DEFAULT_LIMIT_SPEED : speed);
		GLOBAL_RATE_LIMITERS.put(passageId, limiter);
	}

	/**
	 * TODO 关闭原有的代理连接(错误或无效连接，需要重新开启新连接)
	 * 
	 * @param passageId
	 * @return
	 */
	private void closeProxyIfAlive(String passageId) {
		Object manageProxy = getManageProxy(passageId);
		if (manageProxy == null) {
			// ignored if proxy is null
			return;
		}

		try {
			// close if alive
			if (manageProxy instanceof CmppManageProxy) {
				((CmppManageProxy) manageProxy).close();
			} else if (manageProxy instanceof Cmpp3ManageProxy) {
				((Cmpp3ManageProxy) manageProxy).close();
			} else if (manageProxy instanceof SmgpManageProxy) {
				((SmgpManageProxy) manageProxy).close();
			} else if (manageProxy instanceof SgipManageProxy) {
				((SgipManageProxy) manageProxy).close();
			} else {
				logger.error("Closing manageProxy[" + manageProxy + "] failed cause by unkown instance");
			}

		} catch (Exception e) {
			logger.warn("Close proxy failed cause by msssage[" + e.getMessage() + "]");
		}
	}

	/**
	 * TODO 生成新代理实例
	 * 
	 * @param passageId
	 * @param tparameter
	 * @param protocolType
	 * @return
	 */
	private Object newProxy(String passageId, TParameter tparameter, ProtocolType protocolType) {
		Object proxy = null;
		switch (protocolType) {
		case CMPP2: {
			proxy = new CmppManageProxy(cmppProxySender, passageId, new Args(tparameter.getCmppConnectAttrs()));
			break;
		}
		case CMPP3: {
			proxy = new Cmpp3ManageProxy(cmpp3ProxySender, passageId, new Args(tparameter.getCmppConnectAttrs()));
			break;
		}
		case SMGP: {
			proxy = new SmgpManageProxy(smgpProxySender, passageId, new Args(tparameter.getSmgpConnectAttrs()));
			break;
		}
		case SGIP: {
			proxy = loadSgipManageProxy(passageId, tparameter.getSgipConnectAttrs());
			break;
		}
		default: {
			throw new RuntimeException(
					"ProtocolType[" + protocolType.name() + "] not belong to gateway direct protocol.");
		}
		}

		return proxy;
	}

	/**
	 * TODO 加载代理信息
	 * 
	 * @param passageId
	 * @param tparameter
	 * @param protocolType
	 * @return
	 */
	private boolean loadManageProxy(String passageId, TParameter tparameter, ProtocolType protocolType) {

		closeProxyIfAlive(passageId);

		try {

			// 获取代理实例
			Object proxy = newProxy(passageId, tparameter, protocolType);

			if (proxy == null) {
				logger.error("Loading direct proxy failed, proxy is null");
				return false;
			}

			// 加载通道代理类信息
			GLOBAL_PROXIES.put(passageId, proxy);

			logger.info("Passage[" + passageId + "] protocol[" + protocolType.name() + "]  setup succeed with args ["
					+ JSON.toJSONString(tparameter) + "]");

			return true;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * TODO 加载联通SGIP代理信息
	 * 
	 * @param passageId
	 * @param attrs
	 * @return
	 */
	private SgipManageProxy loadSgipManageProxy(String passageId, Map<String, Object> attrs) {
		SgipManageProxy sgipManageProxy = null;
		try {
			// 如果代理类为空则重新初始化
			sgipManageProxy = new SgipManageProxy(sgipProxySender, passageId, new Args(attrs));
			sgipManageProxy.connect(attrs.get("login-name") == null ? "" : attrs.get("login-name").toString(),
					attrs.get("login-pass") == null ? "" : attrs.get("login-pass").toString());

			if (!sgipManageProxy.getConn().available()) {
				if (sgipManageProxy != null && sgipManageProxy.getConn() != null) {
					sgipManageProxy.stopService();
					sgipManageProxy.close();
				}

				throw new DataEmptyException("Sgip proxy load failed");
			}

			return sgipManageProxy;
		} catch (Exception e) {
			if (e instanceof BindException) {
				// 如果错误信息为绑定异常，则睡眠1秒后，递归重试方法
				logger.error("Sgip bind failed, msg[" + e.getMessage() + "] ,it will sleep 1000 ms, retry..");
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e1) {
				}
				return loadSgipManageProxy(passageId, attrs);
			}

			if (sgipManageProxy != null && sgipManageProxy.getConn() != null) {
				sgipManageProxy.stopService();
				sgipManageProxy.close();
			}

			throw new DataEmptyException("Sgip proxy connection failed", e);
		}
	}

	public boolean isProxyAvaiable(String passageId) {
		Object passage = getManageProxy(passageId);
		if (passage == null) {
			return false;
		}

		// if (passage.getConn() == null) {
		// return false;
		// }

		if (passage instanceof SgipManageProxy) {
			SgipManageProxy passageProxy = (SgipManageProxy) passage;

			// 上次发送时间，如果上次发送时间和当前时间差值在55秒内，则认为连接有效
			Long lastSendTime = SgipConstant.SGIP_RECONNECT_TIMEMILLS.get(passageId);

			logger.info("SGIP 状态：{}, 上次时间：{}， 时间差：{} ms", passageProxy.getConnState(), lastSendTime,
					lastSendTime == null ? null : System.currentTimeMillis() - lastSendTime);
			if (lastSendTime != null && (System.currentTimeMillis() - lastSendTime > SGIP_RECONNECT_TIMEOUT)) {
				return false;
			}

			if (StrUtil.isNotEmpty(passageProxy.getConnState())) {
				logger.error("SGIP连接错误，错误信息：{} ", passageProxy.getConnState());
				return false;
			}

		}

		// 判断该通道发送错误是否累计3次，如果累计三次，返回FALSE，需要重连 add by 20170903
		if (GLOBAL_PROXIES_ERROR_COUNTER.get(passageId) != null && GLOBAL_PROXIES_ERROR_COUNTER.get(passageId) >= 3) {
			logger.info("当前通道ID： {} 发送错误次数：{}", passageId, GLOBAL_PROXIES_ERROR_COUNTER.get(passageId));
			return false;
		}

		return true;
	}

	/**
	 * TODO 获取CMPP代理
	 * 
	 * @param passageId
	 * @return
	 */
	public static Object getManageProxy(String passageId) {
		return GLOBAL_PROXIES.get(passageId);
	}

	public boolean stopProxy(String passageId) {
		logger.info("stopProxy, passageId : {} ", passageId);
		if (!isProxyAvaiable(passageId)) {
			logger.warn("PassageId [" + passageId + "] has shutdown ");
			return true;
		}

		try {
			Object prxoy = GLOBAL_PROXIES.get(passageId);
			if (prxoy instanceof SgipManageProxy) {
				SgipManageProxy sgipManageProxy = (SgipManageProxy) prxoy;
				sgipManageProxy.stopService();
			}

			closeProxyIfAlive(passageId);
			logger.info("Passage[" + passageId + "] shutdown finished");
			return true;
		} catch (Exception e) {
			logger.error("停止代理异常", e);
			return false;
		}
	}

	public void plusSendErrorTimes(String passageId) {
		synchronized (GLOBAL_PROXIES_ERROR_COUNTER) {
			Integer counter = GLOBAL_PROXIES_ERROR_COUNTER.get(passageId);
			if (counter == null) {
				counter = 0;
			}

			logger.info("当前通道 passageId : {} 代理失败次数 {}", passageId, counter + 1);
			GLOBAL_PROXIES_ERROR_COUNTER.put(passageId, counter + 1);
		}
	}

	public void clearSendErrorTimes(String passageId) {
		synchronized (GLOBAL_PROXIES_ERROR_COUNTER) {
			Integer counter = GLOBAL_PROXIES_ERROR_COUNTER.get(passageId);
			if (counter == null) {
				return;
			}

			GLOBAL_PROXIES_ERROR_COUNTER.put(passageId, 0);
		}
	}
}
