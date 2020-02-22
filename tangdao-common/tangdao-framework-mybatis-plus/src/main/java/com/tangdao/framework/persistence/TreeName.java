/**
 * 
 */
package com.tangdao.framework.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * TODO 用于通过注解获取节点的名称，树结构的数据库操作中使用
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeName {

	/**
     * 字段值（驼峰命名方式,该值可无）
     */
    String value() default "";

    /**
     * 是否为数据库表字段
     * 默认 true 存在，false 不存在
     */
    boolean exist() default true;
}
