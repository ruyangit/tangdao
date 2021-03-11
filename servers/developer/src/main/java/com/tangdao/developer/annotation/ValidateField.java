package com.tangdao.developer.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateField {

	String value() default "";

	/**
	 * 
	 * 是否必填项
	 * 
	 * @return
	 */
	boolean required() default false;

	/**
	 * 
	 * 是否为数字
	 * 
	 * @return
	 */
	boolean number() default false;

	/**
	 * 
	 * 是否必须UTF-8编码（暂未用）
	 * 
	 * @return
	 */
	boolean utf8() default false;
}
