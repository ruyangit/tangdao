/**
 *
 */
package com.tangdao.core.web.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.bind.annotation.PostMapping;

import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Aspect
public class DemoAspect {

	@Pointcut("@within(postMapping) || @annotation(postMapping)")
	public void demoPointcut(PostMapping postMapping) {

	}

	@Before("demoPointcut(postMapping)")
	public void doAround(JoinPoint joinPoint, PostMapping postMapping) throws Throwable {
		throw new BusinessException(CommonApiCode.COMMON_DEMO);
	}
}
