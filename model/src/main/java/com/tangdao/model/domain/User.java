/**
 *
 */
package com.tangdao.model.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tangdao.model.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Getter
@Setter
@TableName("sys_user")
public class User extends DataEntity<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String username;
	
	private String password;
	
	private String nickname;
	
	private String userType;
	
	private String phone;
	
	private String address;
	
	private String zipCode;
	
	private String companyName;
	
	private String companyCode;
	
	private String wechat;
	
	private String dingding;
	
	private String mgrType;
	
	private String status;
	
	private String remarks;
	
	private String lastLoginIp;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastLoginDate;

}
