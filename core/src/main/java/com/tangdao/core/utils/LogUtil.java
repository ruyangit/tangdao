/**
 *
 */
package com.tangdao.core.utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.context.SessionContext;
import com.tangdao.core.model.SessionUser;
import com.tangdao.core.model.SysLog;
import com.tangdao.core.service.ILogService;
import com.tangdao.core.web.SpringUtils;
import com.tangdao.core.web.filter.BodyReaderHttpServletRequestWrapper;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;

/**
 * <p>
 * TODO 日志工具
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
public class LogUtil {
	
	/**
	 * 静态内部类，延迟加载，懒汉式，线程安全的单例模式
	 */
	private static final class Static {
		private static ILogService logService = SpringUtils.getBean(ILogService.class);
		private static TaskExecutor taskExecutor = SpringUtils.getBean(TaskExecutor.class);
	}

	/**
	 * 异常信息
	 * 
	 * @param exceptionName
	 * @param exceptionMessage
	 * @param elements
	 * @return
	 */
	private static String stackTraceToString(String exceptionName, String exceptionMessage,
			StackTraceElement[] elements) {
		StringBuffer strbuff = new StringBuffer();
		for (StackTraceElement stet : elements) {
			strbuff.append(stet + "\n");
		}
		return exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
	}

	/**
	 * 
	 * TODO 保存日志
	 * 
	 * @param logTitle
	 * @param logType
	 */
	public static void saveLog(String logTitle, String logType) {
		saveLog(ServletUtil.getRequest(), null, logTitle, logType, null, 0);
	}

	/**
	 * 
	 * TODO 保存日志
	 * 
	 * @param request
	 * @param handler
	 * @param logTitle
	 * @param logType
	 * @param throwable
	 * @param executeTime
	 */
	public static void saveLog(HttpServletRequest request, Object handler, String logTitle, String logType,
			Throwable throwable, long executeTime) {
		HandlerMethod handlerMethod = null;
		if (handler instanceof HandlerMethod && (handlerMethod = (HandlerMethod) handler) != null) {
			// 日志标题
			LogOpt logOpt = handlerMethod.getMethod().getAnnotation(LogOpt.class);
			if (logOpt != null && StrUtil.isNotBlank(logTitle)) {
				logTitle = handlerMethod.getMethod().getAnnotation(LogOpt.class).logTitle();
			}
			// 不需要日志
			if (logOpt != null && logOpt.needLog()) {
				return;
			}
		}
		if (StrUtil.isEmpty(logTitle)) {
			logTitle = "";
		}
		// 对象
		SysLog log = new SysLog();
		log.setLogTitle(logTitle);
		log.setLogType(logType);
		if (StrUtil.isEmpty(logType)) {
			String sqlCommandTypes = ObjectUtil.toString(request.getAttribute(SqlCommandType.class.getName()));
			if (StrUtil.containsAny("," + sqlCommandTypes + ",", ",INSERT,", ",UPDATE,", ",DELETE,")) {
				log.setLogType(SysLog.TYPE_UPDATE);
			} else if (StrUtil.contains("," + sqlCommandTypes + ",", ",SELECT,")) {
				log.setLogType(SysLog.TYPE_SELECT);
			} else {
				log.setLogType(SysLog.TYPE_ACCESS);
			}
		}
		log.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
		log.setRemoteAddr(NetUtil.getLocalhostStr());
		log.setUserAgent(request.getHeader("User-Agent"));
		UserAgent userAgent = UserAgentUtil.parse(log.getUserAgent());
		log.setDeviceName(userAgent.getOs().getName());
		log.setBrowserName(userAgent.getBrowser().getName());
		log.setRequestUri(StrUtil.maxLength(request.getRequestURI(), 255));
		log.setRequestMethod(request.getMethod());
		log.setRequestParams(request.getParameterMap());

		// 描述
		if (handlerMethod != null) {
			log.setDescription(handlerMethod.toString());
		}

		// POST（JSON）
		if (handlerMethod != null && StrUtil.isBlank(log.getRequestParams())) {
			try {
				BodyReaderHttpServletRequestWrapper rw = new BodyReaderHttpServletRequestWrapper(request);
//				log.setRequestParams(StrUtil.maxLength(JSON.toJSONString(rw.getRequestBody()), 5000));
				log.setRequestParams(StrUtil.maxLength(StrUtil.trim(
						Base64.decodeStr(JSON.toJSONString(rw.getRequestBody())).replace("\n", "").replace("\t", "")),
						5000));
			} catch (IOException e) {}
		}
		// 耗时
		log.setExecuteTime(executeTime);

		// 操作用户
		SessionUser user = SessionContext.get();
		if (user != null) {
			log.setCreateBy(user.getId());
			log.setCreateByName(user.getUsername());
		}
		// 操作时间
		log.setCreateDate(new Date());

		// 是否异常
		log.setIsException(throwable == null ? Global.YES : Global.NO);
		if (throwable != null) {
			log.setExceptionInfo(stackTraceToString(throwable.getClass().getName(), throwable.getMessage(),
					throwable.getStackTrace()));
		}

		// 如果无地址并无异常日志，则不保存信息
		if (StrUtil.isBlank(log.getRequestUri()) && StrUtil.isBlank(log.getExceptionInfo())) {
			return;
		}

		// 保存日志
		Static.taskExecutor.execute(() -> {
			Static.logService.save(log);
		});

	}

}
