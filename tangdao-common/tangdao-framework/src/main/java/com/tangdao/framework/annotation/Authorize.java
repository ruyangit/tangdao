/**
 * 
 */
package com.tangdao.framework.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * <p>
 * TODO 细粒度权限控制
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月24日
 */
@Target({ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Authorize {
	
	@AliasFor("action")
	String[] value() default {};

	/**
	 * 只允许访问的角色编码
	 * 
	 * @return
	 */
	String[] role() default {};

	/**
	 * 只允许访问的授权编码
	 * 
	 * @return
	 */
	@AliasFor("action")
	String[] permission() default {};

	/**
	 * 是否忽略，忽略后不再进行权限控制 true 忽略
	 * 
	 * @return
	 */
	boolean ignore() default false;

	/**
	 * 数据权限控制
	 * 
	 * @return
	 */
	DataAccess dataAccess() default @DataAccess(ignore = true);

	/**
	 * 权限访问日志
	 * 
	 * @return
	 */
	Log log() default @Log(ignore = true);
}
