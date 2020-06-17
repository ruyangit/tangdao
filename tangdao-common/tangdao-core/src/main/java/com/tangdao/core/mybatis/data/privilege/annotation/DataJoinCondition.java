package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;

/**
 * 
 * @ClassName: DataJoinCondition.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataJoinCondition {
	
	/**
	 * set columns.
	 * 
	 * @return
	 * @return DataColumn
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataColumn[] columns();
	
	/**
	 * set combineType.
	 * 
	 * @return
	 * @return CombineType
	 * @author Naughty Guo Jun 2, 2016
	 */
	CombineType combineType() default CombineType.AND;;
}
