/**
 *
 */
package com.tangdao.core.auth;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Data
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Unique string representing user
	 */
	private String username;
}
