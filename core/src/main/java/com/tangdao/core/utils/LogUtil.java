/**
 *
 */
package com.tangdao.core.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.mapping.SqlCommandType;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;

import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.model.domain.Log;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.service.LogService;
import com.tangdao.core.web.SpringUtils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
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
	private String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
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
	public void saveLog(HttpServletRequest request, Object handler, String logTitle, String logType,
			Throwable throwable, long executeTime) {

		Log log = new Log();
		if (handler instanceof HandlerMethod) {
			HandlerMethod hm = ((HandlerMethod) handler);
			Method m = hm.getMethod();
			LogOpt logOpt = m.getAnnotation(LogOpt.class);
			if (logOpt != null && logOpt.ignore()) {
				return;
			}
			
			if (logOpt != null && StrUtil.isNotBlank(logTitle)) {
				logTitle = logOpt.logTitle();
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
//		log.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
//		log.setRemoteAddr(IpUtil.getClientIp(request));
//		UserAgent userAgent = UserAgentUtils.getUserAgent(request);
//		log.setDeviceName(userAgent.getOperatingSystem().getName());
//		log.setBrowserName(userAgent.getBrowser().getName());
//		log.setUserAgent(request.getHeader("User-Agent"));
//		log.setRequestUri(StringUtils.abbr(request.getRequestURI(), 255));
//		log.setRequestParams(request.getParameterMap());
//		log.setRequestMethod(request.getMethod());
//		log.setExecuteTime(executeTime);
//		log.setCreateBy(user.getUserCode());
//		log.setCreateByName(user.getNickname());
//		log.setCreateDate(new Date());
        
		
//		audit.setRequestParams(requestParams);
//		audit.setOperation(operation);
//
//		audit.setClassName(className);
//		// 获取请求的方法名
//		audit.setMethodName(methodName);
//
//		TSession session = SessionContext.getSession();
//		if (session != null) {
//			audit.setCreateBy((String) session.getUserId());
//			audit.setCreateByName((String) session.getUsername());
//		}
//
//		audit.setExecuteTime(executeTime);
//
//		audit.setIsException(throwable != null ? CommonContext.YES : CommonContext.NO);
//		if (throwable != null) {
//			audit.setExceptionName(throwable.getClass().getName());
//			audit.setExceptionInfo(stackTraceToString(throwable.getClass().getName(), throwable.getMessage(),
//					throwable.getStackTrace()));
//		}
//		audit.setRemoteAddr(request.getRemoteAddr());
//		audit.setRequestMethod(request.getMethod());
//		audit.setRequestUri(request.getRequestURI());
//		audit.setServerAddr(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort());
//
//		audit.setUserAgent(request.getHeader("User-Agent"));
//		UserAgent userAgent = UserAgentUtil.parse(audit.getUserAgent());
//		audit.setBrowserName(userAgent.getBrowser().getName());
//		audit.setDeviceName(userAgent.getOs().getName());
//
//		// 保存日志
//		taskExecutor.execute(() -> {
//			auditLogService.saveAuditLog(audit);
//		});

	}

}
