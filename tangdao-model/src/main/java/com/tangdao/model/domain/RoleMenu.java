/**
 *
 */
package com.tangdao.model.domain;

import java.io.Serializable;

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
public class RoleMenu implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String roleCode;

	private String menuCode;
}
