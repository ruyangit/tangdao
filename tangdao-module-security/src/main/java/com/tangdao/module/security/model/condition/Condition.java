/**
 * 
 */
package com.tangdao.module.security.model.condition;

import java.util.HashMap;

import com.tangdao.module.security.model.Request;

/**
 * <p>
 * TODO 抽象条件
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public abstract class Condition extends HashMap<String, Object>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	abstract public Boolean fullfills(String conditionKey, Request request);
}
