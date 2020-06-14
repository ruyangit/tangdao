/**
 *
 */
package com.tangdao.model.dto;

import java.util.List;

import com.tangdao.model.domain.User;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Getter
@Setter
public class UserDTO extends User{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String roleId;
	
	private List<String> roleIds;
	
	private String superAdmin;
	
	private String oldUsername;
}
