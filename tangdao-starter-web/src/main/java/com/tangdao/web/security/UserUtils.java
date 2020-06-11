/**
 *
 */
package com.tangdao.web.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.tangdao.web.security.user.SecurityUser;
import com.tangdao.web.security.user.SecurityUserDetails;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
public class UserUtils {

	public static String getUserId() {
		SecurityUser user = getPrincipal();
		if (user != null) {
			return user.getId();
		}
		return null;
	}
	
	public static boolean isa() {
		SecurityUser user = getPrincipal();
		if (user != null) {
			return user.isa();
		}
		return false;
	}

	public static SecurityUser getPrincipal() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}

		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof SecurityUserDetails) {
			return ((SecurityUserDetails) principal).getSecurityUser();
		}
		return null;
	}
}
