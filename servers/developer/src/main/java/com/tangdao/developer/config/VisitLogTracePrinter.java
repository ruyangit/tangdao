package com.tangdao.developer.config;

import java.util.concurrent.atomic.AtomicLong;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Aspect
@Order(1)
@Component
public class VisitLogTracePrinter {

	AtomicLong startTime = new AtomicLong();
	Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("execution(public * com.tangdao.developer.*.prervice..*.*(..))")
	public void pointcut() {
	}

	@Before("pointcut()")
	public void doBefore(JoinPoint joinPoint) throws Throwable {
		logger.info("调用方法：{}, 参数信息： {}", joinPoint.getSignature().getName(), JSON.toJSONString(joinPoint.getArgs()));
		startTime.set(System.currentTimeMillis());
	}

	@AfterReturning(returning = "ret", pointcut = "pointcut()")
	public void doAfterReturning(Object ret) throws Throwable {
		// 处理完请求，返回内容
		logger.info("响应数据：{}, 处理耗时 : {} 毫秒", ret, (System.currentTimeMillis() - startTime.get()));
		logger.info("---------------------------------");
	}
}
