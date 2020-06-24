package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.OperationType;

/**
 * 
 * @ClassName: DataJoinColumn.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataJoinColumn {
	
	/**
	 * master object column name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String masterColumn();
	
	/**
	 * slave object column name.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String slaveColumn();
	
	/**
	 * set operationType.
	 * 
	 * @return
	 * @return OperationType
	 * @author Naughty Guo Jun 2, 2016
	 */
	OperationType operationType() default OperationType.EQUAL;
}
