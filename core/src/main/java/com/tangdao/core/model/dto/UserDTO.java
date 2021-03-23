/**
 *
 */
package com.tangdao.core.model.dto;

import java.io.Serializable;

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
public class UserDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;

	private String oldUsername;

	private String roleIds;

}
