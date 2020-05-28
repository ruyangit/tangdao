/**
 *
 */
package com.tangdao.model.base;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;

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
@TableName("user")
public class UserModel extends Model<UserModel>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private Long id;
	
	private String username;
	
	private String nickname;
	
	private String realname;
	
	private String password;
	
	private String salt;
	
	private String mobile;
	
	private String email;
	
	private String gender;
	
	private String signature;
	
	private String avatar;
	
	private String remark;
	
	private String status;
	
	private Date created;
	
	private Date createSource;
	
	private Date modified;
	
	private Date activated;
	
	private Date lastLoginDate;
	
	private String lastLoginIp;
}
