package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: DataSqlSegment.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataSqlSegment {

	/**
	 * segment placeholder name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String val();
	
	/**
	 * SQL segment.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String sql();
	
	/**
	 * the values which had been set.
	 * 
	 * @return
	 * @return String[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	String[] values() default {};
	
	/**
	 * the key which map provider contains.<br>
	 * get values from the map provider by keys.
	 * 
	 * @return
	 * @return String[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	String[] categoryKeys() default {};
}
