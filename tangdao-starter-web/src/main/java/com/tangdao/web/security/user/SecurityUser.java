/**
 *
 */
package com.tangdao.web.security.user;

import com.tangdao.core.auth.User;

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
public class SecurityUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String token;

	private boolean superAdmin = false;
}
