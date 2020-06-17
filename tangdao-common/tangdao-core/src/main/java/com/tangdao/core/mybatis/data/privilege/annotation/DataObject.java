package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: DataObject.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataObject {

	/**
	 * object name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String name();
	
	/**
	 * object alias name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String alias();
}
