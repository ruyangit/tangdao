/**
 *
 */
package com.tangdao.web.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月3日
 */
@Component
public class UserPermissionService {

	public boolean hasPermission(Authentication authentication, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return true;
	}
}
