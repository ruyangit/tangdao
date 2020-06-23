/**
 *
 */
package com.tangdao.model.dto;

import java.util.List;

import com.tangdao.model.domain.RoleMenu;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月23日
 */
@Getter
@Setter
public class RoleMenuDTO extends RoleMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> menuIds;
}
