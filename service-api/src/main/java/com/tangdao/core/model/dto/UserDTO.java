/**
 *
 */
package com.tangdao.core.model.dto;

import java.util.List;

import com.tangdao.core.model.domain.User;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月23日
 */
@Getter
@Setter
public class UserDTO extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String oldUsername;
	
	private String roleId;

	private List<String> roleIds;
	
}
