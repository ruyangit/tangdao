/**
 *
 */
package com.tangdao.core.web.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataRule {

    String name() default "";

    DataRuleStrategy strategy() default DataRuleStrategy.TEXT;

    String url() default "";

    Class<? extends IDataRuleProvider> provider() default NullDataRuleProvider.class;
}
