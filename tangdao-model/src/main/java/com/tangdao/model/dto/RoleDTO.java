/**
 * 
 */
package com.tangdao.model.dto;

import java.util.List;

import com.tangdao.model.domain.Role;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年6月14日
 */
@Getter
@Setter
public class RoleDTO extends Role {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String oldRoleName;
	
	private List<String> menuIds;

}
