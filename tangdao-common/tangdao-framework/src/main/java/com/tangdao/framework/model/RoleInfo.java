/**
 * 
 */
package com.tangdao.framework.model;

import java.io.Serializable;

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
	
	private String roleName;
	
	private String viewCode;

	private String userId;

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
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getViewCode() {
		return viewCode;
	}

	public void setViewCode(String viewCode) {
		this.viewCode = viewCode;
	}
	
}
