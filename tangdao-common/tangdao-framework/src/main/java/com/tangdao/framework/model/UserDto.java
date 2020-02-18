package com.tangdao.framework.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tangdao.framework.model.entity.BaseEntity;

public class UserDto extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户编码
	 */
	private String userCode;
	
	/**
	 * 登录账号
	 */
	private String username;

	/**
	 * 登录密码
	 */
	@JsonIgnore
	private String password;

	/**
	 * 电子邮箱
	 */
	private String email;

	/**
	 * 手机号码
	 */
	private String mobile;

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
