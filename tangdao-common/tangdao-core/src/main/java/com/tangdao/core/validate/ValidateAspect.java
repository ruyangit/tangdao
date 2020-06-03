/**
 *
 */
package com.tangdao.core.validate;


import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tangdao.common.utils.ReflectUtils;

import cn.hutool.core.util.ReflectUtil;

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
	 * 用于获取方法参数定义名字.
	 */
	private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

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
	public void doAround(ProceedingJoinPoint joinPoint, Validate validate) throws Throwable {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		String[] paramNames = nameDiscoverer.getParameterNames(methodSignature.getMethod());
		
		for(Field field: validate.value()) {
//			Field fields = validate.value()[i];
			String fieldName = field.name();
			Object fieldValue = null;
			
			for(int c=0; c<joinPoint.getArgs().length; c++) {
				Object fvo = joinPoint.getArgs()[c];
				if(fieldName.equals(paramNames[c])) {
					fieldValue = fvo;
					break;
				}
//				fieldValue = ReflectUtils.invokeGetter(joinPoint.getArgs()[c], fieldName);
				
//				fieldValue = ReflectUtil.getFieldValue(fvo, fieldName);
//				if(fieldValue!=null) {
//					break;
//				}
				boolean vfs = false;
				for (String name : StringUtils.split(fieldName, ".")){
//					String getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name);
//					object = invokeMethod(object, getterMethodName, new Class[] {}, new Object[] {});
//					System.out.println(name);
					ReflectUtils.getFieldValue(fvo, name);
					fieldValue = ReflectUtil.getFieldValue(fvo, fieldName);
					if(fieldValue!=null) {
						vfs = true;
						break;
					}
				}
				
				if(vfs) {
					break;
				}
			}
			System.out.println(fieldName);
			System.out.println(fieldValue);
			for(Rule rule: field.rules()) {
//				Method method = ReflectUtil.getMethod(Validator.class, "isNull()");
//				Object obj = ReflectUtil.invoke(null, "cn.hutool.core.lang.isNull(val)", fieldValue);
//				System.out.println(obj);
				System.out.println(rule);
				System.out.println(rule.type().getValue());
			}
			System.out.println("----");
		}
		
//		System.out.println(JSON.toJSONString(validate));
//		System.out.println(JSON.toJSONString(argsMap));
	}
}
