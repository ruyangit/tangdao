/**
 * 
 */
package com.tangdao.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * TODO 日志记录配置
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月24日
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {

	/**
	 * 是否忽略权限日志
	 * @return
	 */
	boolean ignore() default false;
	
	/**
	 * 是否保存到数据库
	 * @return
	 */
	boolean db() default false;
}
