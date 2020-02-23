/**
 * 
 */
package com.tangdao.framework.model;

import java.io.Serializable;
import java.util.List;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
public class UserVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;
	
	private String loginName;
	
	private String password;
	
	private String tenantId;
	
	private String status;
	
	private List<RoleVo> roles = CollUtil.newArrayList();
	
	private List<String> authorities;

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
	 * @return the loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * @param loginName the loginName to set
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the roles
	 */
	public List<RoleVo> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<RoleVo> roles) {
		this.roles = roles;
	}

	/**
	 * @return the authorities
	 */
	public List<String> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
	
}
