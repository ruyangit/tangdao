/**
 *
 */
package com.tangdao.core.web.aspect;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.NamedThreadLocal;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;

import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.utils.LogUtil;
import com.tangdao.core.utils.ServletUtil;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 操作日志切面
 * </p>
 *
 * @author ruyang
 * @since 2021年4月6日
 */
@Aspect
@ConditionalOnClass({ HttpServletRequest.class, RequestContextHolder.class })
public class LogAspect {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 本地线程
	 */
	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("LogAspectThreadLocal");

	/**
	 * 用于SpEL表达式解析.
	 */
	private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	/**
	 * 用于获取方法参数定义名字.
	 */
	private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	/**
	 * 解析spEL表达式
	 */
	private String getValBySpEL(String spEL, Map<String, Object> argsMap) {
		// 获取方法形参名数组
		if (argsMap != null) {
			Expression expression = spelExpressionParser.parseExpression(spEL);
			// spring的表达式上下文对象
			EvaluationContext context = new StandardEvaluationContext();
			// 给上下文赋值
			argsMap.entrySet().forEach(r -> {
				context.setVariable(r.getKey(), r.getValue());
			});
			return expression.getValue(context).toString();
		}
		return null;
	}

	/**
	 * 获取所有参数
	 * 
	 * @param methodSignature
	 * @param args
	 * @return
	 */
	private Map<String, Object> getArgsMap(MethodSignature methodSignature, Object[] args) {
		// 获取方法形参名数组
		String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
		if (paramNames != null && paramNames.length > 0) {
			// spring的表达式上下文对象
			Map<String, Object> argsMap = new LinkedHashMap<String, Object>();
			// 给上下文赋值
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse
						|| args[i] instanceof Model) {
					continue;
				}
				argsMap.put(paramNames[i], args[i]);
			}
			return argsMap;
		}
		return null;
	}

	private void saveLogOpt(JoinPoint joinPoint, LogOpt logOpt, Object data, Throwable throwable) {
		// 判断功能是否开启
		if (startTimeThreadLocal.get() == null) {
			// 不保存日志
			return;
		}
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间
		long executeTime = endTime - beginTime; // 3、获取执行时间
		startTimeThreadLocal.remove(); // 用完之后销毁线程变量数据

		if (logOpt == null) {
			// 获取类上的注解
			logOpt = joinPoint.getTarget().getClass().getDeclaredAnnotation(LogOpt.class);
		}
		if (logOpt == null || !logOpt.needLog()) {
			// 不保存日志
			return;
		}
		if (!logOpt.needData()) {
			data = null;
		}

		// 从切面织入点处通过反射机制获取织入点处的方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		String description = logOpt.description();
		if (StrUtil.isNotBlank(description) && description.contains("#")) {
			// 获取方法参数值
			Map<String, Object> argsMap = getArgsMap(signature, joinPoint.getArgs());
			description = getValBySpEL(description, argsMap);
		}
		if (StrUtil.isBlank(description)) {
			description = signature.getDeclaringTypeName() + "#" + signature.getName();
		}
		// 保存数据库
		try {
			LogUtil.saveLog(ServletUtil.getRequest(), logOpt.logTitle(), null, description, data, throwable,
					executeTime);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

	}

	/**
	 * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
	 */
	@Pointcut("@within(logOpt) || @annotation(logOpt)")
	public void LogOptPointcut(LogOpt logOpt) {
	}

	/**
	 * 前置通知： 1. 在执行目标方法之前执行，比如请求接口之前的登录验证; 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
	 *
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("LogOptPointcut(logOpt)")
	public void doBefore(JoinPoint joinPoint, LogOpt logOpt) {
		// 判断功能是否开启
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
	}

	/**
	 * 返回通知： 1. 在目标方法正常结束之后执行 1. 在返回通知中补充请求日志信息，如返回时间，方法耗时，返回值，并且保存日志信息
	 *
	 * @param ret
	 * @throws Throwable
	 */
	@AfterReturning(value = "LogOptPointcut(logOpt)", returning = "data")
	public void doAfterReturning(JoinPoint joinPoint, LogOpt logOpt, Object data) {
		saveLogOpt(joinPoint, logOpt, data, null);
	}

	/**
	 * 异常通知： 1. 在目标方法非正常结束，发生异常或者抛出异常时执行 1. 在异常通知中设置异常信息，并将其保存
	 *
	 * @param throwable
	 */
	@AfterThrowing(value = "LogOptPointcut(logOpt)", throwing = "throwable")
	public void doAfterThrowing(JoinPoint joinPoint, LogOpt logOpt, Throwable throwable) {
		saveLogOpt(joinPoint, logOpt, null, throwable);
	}

}
