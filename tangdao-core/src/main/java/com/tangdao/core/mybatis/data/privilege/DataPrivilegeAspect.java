/**
 *
 */
package com.tangdao.core.mybatis.data.privilege;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;

/**
 * <p>
 * TODO 描述 审计日志切面
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
@Aspect
@Component
public class DataPrivilegeAspect {

	@Pointcut("@annotation(dataPrivilege)")
	public void dataPrivilegePointcut(DataPrivilege dataPrivilege) {
	}

	@Around("dataPrivilegePointcut(dataPrivilege)")
	public Object doAround(ProceedingJoinPoint joinPoint, DataPrivilege dataPrivilege) throws Throwable {
		DataPrivilegeContext.setDataPrivilege(dataPrivilege);
		return joinPoint.proceed();
	}

}
