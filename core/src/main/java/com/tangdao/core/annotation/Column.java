/**
 * 
 */
package com.tangdao.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2021年4月10日
 */
@Retention(RetentionPolicy.RUNTIME)	
@Documented	
public @interface Column {
	String name() default "";
	
	String attrName() default "";	
	
	boolean isPK() default false;	
	
	boolean isTreeName() default false;	
}
