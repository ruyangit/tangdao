/**
 *
 */
package com.tangdao.core.auth;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Getter
@Setter
public class Permission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * An unique key of resource
	 */
	private String resource;

	/**
	 * Action on resource, refer to class ActionTypes
	 */
	private String action;

	public Permission() {

	}

	public Permission(String resource, String action) {
		this.resource = resource;
		this.action = action;
	}
}
