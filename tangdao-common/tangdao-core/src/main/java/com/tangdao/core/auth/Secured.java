/**
 *
 */
package com.tangdao.core.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * <p>
 * TODO Annotation indicating that the annotated request should be authorized.
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

	/**
	 * The action type of the request
	 *
	 * @return action type, default READ
	 */
	ActionTypes action() default ActionTypes.READ;

	/**
	 * The name of resource related to the request
	 *
	 * @return resource name
	 */
	String resource() default StringUtils.EMPTY;

}
