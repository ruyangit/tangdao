/**
 *
 */
package com.tangdao.core.model.dto;

import com.tangdao.core.model.domain.Role;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月24日
 */
@Getter
@Setter
public class RoleDTO extends Role {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String userId;

	private String oldRoleCode;

}
