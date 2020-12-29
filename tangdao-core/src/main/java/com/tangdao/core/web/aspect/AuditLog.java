/**
 *
 */
package com.tangdao.core.web.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
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
