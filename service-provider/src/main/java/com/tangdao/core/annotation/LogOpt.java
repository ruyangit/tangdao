/**
 *
 */
package com.tangdao.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOpt {

	/**
	 * 
	 * TODO 日志标题
	 * @return
	 */
	String logTitle();

	/**
	 * 
	 * TODO 是否需要日志
	 * @return
	 */
	boolean needLog() default false;
}
