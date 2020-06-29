/**
 *
 */
package com.tangdao.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * TODO 审计日志注解， 保存请求信息
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月24日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

	/**
	 * TODO 标题
	 */
	String title();

	/**
	 * TODO 操作信息
	 */
	String operation() default StringUtils.EMPTY;
}
