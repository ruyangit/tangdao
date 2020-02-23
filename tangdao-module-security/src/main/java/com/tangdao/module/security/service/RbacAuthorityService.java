/**
 * 
 */
package com.tangdao.module.security.service;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class RbacAuthorityService {
	/**
	 * 日志服务
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		if(authentication instanceof AnonymousAuthenticationToken) {
			logger.warn("[AnonymousAuthenticationToken] ServletPath: {}", request.getServletPath());
		}
		if(authentication instanceof UserDetails) {
			
		}
		return false;
	}
}
