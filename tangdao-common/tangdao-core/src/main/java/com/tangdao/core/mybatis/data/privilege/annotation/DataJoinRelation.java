package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ObjectJoinType;

/**
 * 
 * @ClassName: DataJoinRelation.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataJoinRelation {

	/**
	 * master object.
	 * 
	 * @return
	 * @return DataObject
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataObject master();
	
	/**
	 * slave object.
	 * 
	 * @return
	 * @return DataObject
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataObject slave();
	
	/**
	 * set join columns.
	 * 
	 * @return
	 * @return DataJoinColumn[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataJoinColumn[] joinColumns() default {};
	
	/**
	 * set join conditions.
	 * 
	 * @return
	 * @return DataJoinCondition[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataJoinCondition[] conditions() default {};
	
	/**
	 * set joinType.
	 * 
	 * @return
	 * @return JoinType
	 * @author Naughty Guo Jun 2, 2016
	 */
	ObjectJoinType joinType() default ObjectJoinType.INNER;
}
