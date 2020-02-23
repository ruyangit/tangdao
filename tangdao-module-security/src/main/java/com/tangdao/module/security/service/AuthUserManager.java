/**
 * 
 */
package com.tangdao.module.security.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tangdao.framework.context.UserManager;
import com.tangdao.framework.model.UserVo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class AuthUserManager implements UserManager {

	@Override
	public UserVo getUserVo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof UserPrincipal) {
			return ((UserPrincipal) principal).getUserVo();
		}
		return null;
	}

}
