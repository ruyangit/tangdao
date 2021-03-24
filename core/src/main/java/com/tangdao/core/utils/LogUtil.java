/**
 *
 */
package com.tangdao.core.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.method.HandlerMethod;

import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.context.SessionContext;
import com.tangdao.core.model.domain.Log;
import com.tangdao.core.model.vo.SessionUser;
import com.tangdao.core.service.LogService;
import com.tangdao.core.web.SpringUtils;

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
		private static LogService logService = SpringUtils.getBean(LogService.class);
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
	private static String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
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
	 * @param request
	 * @param handler
	 * @param logTitle
	 * @param logType
	 * @param throwable
	 * @param executeTime
	 */
	public static void saveLog(HttpServletRequest request, Object handler, String logTitle, String logType,
			Throwable throwable, long executeTime) {
		//
		Log log = new Log();
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = ((HandlerMethod) handler);
			// 标题
			if (hm.getMethod().getAnnotation(LogOpt.class) != null && StrUtil.isNotBlank(logTitle)) {
				logTitle = hm.getMethod().getAnnotation(LogOpt.class).logTitle();
			}
		}
		log.setLogTitle(logTitle);
		log.setLogType(logType);
		if (StrUtil.isEmpty(logType)) {
			String sqlCommandTypes = ObjectUtil.toString(request.getAttribute(SqlCommandType.class.getName()));
			if (StrUtil.containsAny("," + sqlCommandTypes + ",", ",INSERT,", ",UPDATE,", ",DELETE,")) {
				log.setLogType(Log.TYPE_UPDATE);
			} else if (StrUtil.contains("," + sqlCommandTypes + ",", ",SELECT,")) {
				log.setLogType(Log.TYPE_SELECT);
			} else {
				log.setLogType(Log.TYPE_ACCESS);
			}
		}
		log.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
		log.setRemoteAddr(IPUtil.getClientIp(request));
		log.setUserAgent(request.getHeader("User-Agent"));
		UserAgent userAgent = UserAgentUtil.parse(log.getUserAgent());
		log.setDeviceName(userAgent.getOs().getName());
		log.setBrowserName(userAgent.getBrowser().getName());
		log.setRequestUri(StrUtil.maxLength(request.getRequestURI(), 255));
		log.setRequestParams(request.getParameterMap());
		log.setRequestMethod(request.getMethod());
		log.setExecuteTime(executeTime);

		SessionUser user = SessionContext.getSession();
		if (user != null) {
			log.setCreateBy(user.getId());
			log.setCreateByName(user.getUsername());
		}
		log.setCreateDate(new Date());

		log.setIsException(throwable != null ? Global.YES : Global.NO);
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
