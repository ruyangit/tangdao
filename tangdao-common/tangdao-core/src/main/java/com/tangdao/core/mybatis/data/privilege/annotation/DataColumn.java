package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ColumnType;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.OperationType;

/**
 * 
 * @ClassName: DataColumn.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataColumn {
	
	/**
	 * object which the column belong to.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataObject object();
	
	/**
	 * column name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String name();
	
	/**
	 * the key which map provider contains.<br>
	 * get value from the map provider by key.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String categoryKey();
	
	/**
	 * the value which had been set.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String value() default "";
	
	/**
	 * set wrapper value
	 * 
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月12日
	 */
	String wrapper() default "";
	
	/**
	 * set columnType.
	 * 
	 * @return
	 * @return ColumnType
	 * @author Naughty Guo Jun 2, 2016
	 */
	ColumnType columnType() default ColumnType.String;
	
	/**
	 * set operationType.
	 * 
	 * @return
	 * @return OperationType
	 * @author Naughty Guo Jun 2, 2016
	 */
	OperationType operationType() default OperationType.EQUAL;
}
