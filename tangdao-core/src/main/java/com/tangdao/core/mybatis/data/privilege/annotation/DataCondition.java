package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;

/**
 * 
 * @ClassName: DataCondition.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataCondition {
	
	/**
	 * reference object.
	 * 
	 * @return
	 * @return DataObject
	 * @author Naughty Guo Jun 3, 2016
	 */
	DataObject reference();

	/**
	 * set columns property.
	 * 
	 * @return
	 * @return DataColumn
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataColumn[] columns();
	
	/**
	 * group flag.
	 * 
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 2, 2016
	 */
	String categoryGroup() default "";
	
	/**
	 * set group combine type.
	 * 
	 * @return
	 * CombineType
	 * @author Naughty Guo 2016年7月14日
	 */
	CombineType groupCombineType() default CombineType.AND;
	
	/**
	 * set combineType.
	 * 
	 * @return
	 * @return CombineType
	 * @author Naughty Guo Jun 2, 2016
	 */
	CombineType combineType() default CombineType.AND;
}
