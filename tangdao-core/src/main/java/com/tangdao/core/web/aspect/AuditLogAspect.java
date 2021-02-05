/**
 *
 */
package com.tangdao.core.web.aspect;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;

import com.alibaba.fastjson.JSON;

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;

/**
 * <p>
 * TODO 描述 审计日志切面
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
@Aspect
@ConditionalOnClass({ HttpServletRequest.class, RequestContextHolder.class })
public class AuditLogAspect {

	/**
	 * 日志服务
	 */
	private Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 本地线程
	 */
	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("AuditLogAspect");

	/**
	 * 用于SpEL表达式解析.
	 */
	private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();

	/**
	 * 用于获取方法参数定义名字.
	 */
	private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();
	
	/**
	 * 审计日志服务
	 */
	@Resource
	private LogUtils logUtils;
	
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

	private void saveAuditLog(JoinPoint joinPoint, AuditLog auditLog, Object data, Throwable throwable) {
		// 判断功能是否开启
		if (startTimeThreadLocal.get() == null) {
			// 不保存日志
			return;
		}
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间
		long executeTime = endTime - beginTime; // 3、获取执行时间
		startTimeThreadLocal.remove(); // 用完之后销毁线程变量数据

		if (auditLog == null) {
			// 获取类上的注解
			auditLog = joinPoint.getTarget().getClass().getDeclaredAnnotation(AuditLog.class);
		}
		if (auditLog == null) {
			// 不保存日志
			return;
		}

		// 从切面织入点处通过反射机制获取织入点处的方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Map<String, Object> argsMap = getArgsMap(signature, joinPoint.getArgs());
		String operation = auditLog.operation();
		if (operation.contains("#")) {
			// 获取方法参数值
			operation = getValBySpEL(operation, argsMap);
		}

		try {
			logUtils.saveLog(auditLog.title(), null, operation, JSON.toJSONString(argsMap), signature.getDeclaringTypeName(),
					signature.getName(), throwable, executeTime);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage(), e);
		}

	}

	/**
	 * 定义请求日志切入点，其切入点表达式有多种匹配方式,这里是指定路径
	 */
	@Pointcut("@within(auditLog) || @annotation(auditLog)")
	public void auditLogPointcut(AuditLog auditLog) {
	}

	/**
	 * 前置通知： 1. 在执行目标方法之前执行，比如请求接口之前的登录验证; 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
	 *
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Before("auditLogPointcut(auditLog)")
	public void doBefore(JoinPoint joinPoint, AuditLog auditLog) {
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
	@AfterReturning(value = "auditLogPointcut(auditLog)", returning = "data")
	public void doAfterReturning(JoinPoint joinPoint, AuditLog auditLog, Object data) {
		saveAuditLog(joinPoint, auditLog, data, null);
	}

	/**
	 * 异常通知： 1. 在目标方法非正常结束，发生异常或者抛出异常时执行 1. 在异常通知中设置异常信息，并将其保存
	 *
	 * @param throwable
	 */
	@AfterThrowing(value = "auditLogPointcut(auditLog)", throwing = "throwable")
	public void doAfterThrowing(JoinPoint joinPoint, AuditLog auditLog, Throwable throwable) {
		saveAuditLog(joinPoint, auditLog, null, throwable);
	}

}
