package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: DataPrivilege.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
public @interface DataPrivilege {

	/**
	 * set data join relations.
	 * 
	 * @return
	 * @return DataJoinRelation[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataJoinRelation[] relations() default {};
	
	/**
	 * set SQL segments.
	 * 
	 * @return
	 * @return DataSqlSegment[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataSqlSegment[] segments() default {};
	
	/**
	 * set conditions.
	 * 
	 * @return
	 * @return DataCondition[]
	 * @author Naughty Guo Jun 2, 2016
	 */
	DataCondition[] conditions() default {};
}
