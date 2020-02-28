/**
 * 
 */
package com.tangdao.module.security.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * <p>
 * TODO 断言描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public class Assertion {

	private String role;

	private String resource;

	private String action;
	
	private String condition;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public AssertionEffect effect;

	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @return the resource
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * @param resource the resource to set
	 */
	public Assertion setResource(String resource) {
		this.resource = resource;
		return this;
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
	public Assertion setAction(String action) {
		this.action = action;
		return this;
	}

	/**
	 * @return the effect
	 */
	public AssertionEffect getEffect() {
		return effect;
	}

	/**
	 * @param effect the effect to set
	 */
	public Assertion setEffect(AssertionEffect effect) {
		this.effect = effect;
		return this;
	}

	/**
	 * @return the condition
	 */
	public String getCondition() {
		return condition;
	}

	/**
	 * @param condition the condition to set
	 */
	public Assertion setCondition(String condition) {
		this.condition = condition;
		return this;
	}
}
