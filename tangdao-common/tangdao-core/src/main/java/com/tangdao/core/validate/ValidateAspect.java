/**
 *
 */
package com.tangdao.core.validate;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月25日
 */
@Aspect
@Component
public class ValidateAspect {
	
	/**
	 * 用于SpEL表达式解析.
	 */
	private SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
	
	/**
	 * 用于获取方法参数定义名字.
	 */
	private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

	/**
	 * 
	 * @param validate
	 */
	@Pointcut("@within(validate) || @annotation(validate)")
	public void validatePointcut(Validate validate) {
	}

	/**
	 * 前置通知： 1. 在执行目标方法之前执行，比如请求接口之前的登录验证; 2. 在前置通知中设置请求日志信息，如开始时间，请求参数，注解内容等
	 *
	 * @param joinPoint
	 * @throws Throwable
	 */
	@Around("validatePointcut(validate)")
	public Object doAround(ProceedingJoinPoint joinPoint, Validate validate) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
		Object[] paramValues = joinPoint.getArgs();
		// spring的表达式上下文对象
		EvaluationContext context = new StandardEvaluationContext();
		// 封装参数
		for(int i=0; i<paramNames.length; i++) {
			context.setVariable(paramNames[i], paramValues[i]);
		}
		for(Field field: validate.value()) {
			Object fieldValue = spelExpressionParser.parseExpression("#"+field.name()).getValue(context);
			for(Rule rule: field.rules()) {
				RuleParser ruleParser = rule.type().getParser().newInstance();
				if(ruleParser.validate(fieldValue, rule.value())) {
					String message = StrUtil.isEmpty(rule.message())?rule.type().getMessage():rule.message();
					throw new IllegalArgumentException(message);
				}
			}
		}
		return joinPoint.proceed();
	}
}
