/**
 *
 */
package com.tangdao.model.domain;

import java.util.Date;

import com.tangdao.model.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@Getter
@Setter
public class User extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	
	private String nickname;
	
	private String realname;
	
	private String password;
	
	private String mobile;
	
	private String email;
	
	private String gender;
	
	private String signature;
	
	private String avatar;
	
	private String remark;
	
	private String status;
	
	private Date createSource;
	
	private Date modified;
	
	private Date activated;
	
	private Date lastLoginDate;
	
	private String lastLoginIp;
	
}
