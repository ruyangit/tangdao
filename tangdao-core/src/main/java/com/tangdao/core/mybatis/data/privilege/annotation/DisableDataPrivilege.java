package com.tangdao.core.mybatis.data.privilege.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @ClassName: DisableDataPrivilege.java
 * @author: Naughty Guo
 * @date: Mar 8, 2018
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DisableDataPrivilege {

}
