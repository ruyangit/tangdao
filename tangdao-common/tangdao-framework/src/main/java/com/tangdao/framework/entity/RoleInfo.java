/**
 * 
 */
package com.tangdao.framework.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * TODO 角色信息
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public class RoleInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleId;
	
	private String roleCode;
	
	private List<Policy> policies;

	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public RoleInfo setRoleId(String roleId) {
		this.roleId = roleId;
		return this;
	}

	/**
	 * @return the roleCode
	 */
	public String getRoleCode() {
		return roleCode;
	}

	/**
	 * @param roleCode the roleCode to set
	 */
	public RoleInfo setRoleCode(String roleCode) {
		this.roleCode = roleCode;
		return this;
	}

	/**
	 * @return the policies
	 */
	public List<Policy> getPolicies() {
		return policies;
	}

	/**
	 * @param policies the policies to set
	 */
	public RoleInfo setPolicies(List<Policy> policies) {
		this.policies = policies;
		return this;
	}
	
}
