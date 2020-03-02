/**
 * 
 */
package com.tangdao.module.security.model;

import java.util.Properties;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public class Request {

	// Resource is the resource that access is requested to.
	public String resource;

	// Action is the action that is requested on the resource.
	public String action;

	// Principal is the principal that is requesting access.
	public UserPrincipal principal;

	// Context is the request's environmental context.
	public Properties context = new Properties();

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public void setResource(String resource) {
		this.resource = resource;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the principal
	 */
	public UserPrincipal getPrincipal() {
		return principal;
	}

	/**
	 * @param principal the principal to set
	 */
	public void setPrincipal(UserPrincipal principal) {
		this.principal = principal;
	}

	/**
	 * @return the context
	 */
	public Properties getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(Properties context) {
		this.context = context;
	}
	
	/**
	 * add context
	 * @param key
	 * @param value
	 * @return Properties
	 */
	public Properties addContext(Object key, Object value) {
		context.put(key, value);
		return context;
	}

}
