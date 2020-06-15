/**
 *
 */
package com.tangdao.web.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.exception.BusinessException;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.modules.sys.service.UserService;
import com.tangdao.web.security.user.SecurityUser;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Component
public class AuthManager {

	@Autowired
	private JwtTokenManager tokenManager;
	
	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public SecurityUser login(HttpServletRequest request) {
		String token = resolveToken(request);
		try {
			tokenManager.validateToken(token);
		} catch (ExpiredJwtException ex) {
			throw new BusinessException(CommonApiCode.USER_TOKEN_EXPIRE, ex);
		} catch (Exception ex) {
			throw new BusinessException(CommonApiCode.UNAUTHORIZED, ex);
		}

		Authentication authentication = tokenManager.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

//		String username = authentication.getName();
//		SecurityUser user = new SecurityUser();
//		user.setUsername(username);
//		user.setToken(token);
//		user.setSuperAdmin(superAdmin.equals(username));
		return (SecurityUser)authentication.getPrincipal();
	}

	/**
	 * Get token from header
	 */
	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(AuthConfig.AUTHORIZATION_HEADER);
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(AuthConfig.TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}
		bearerToken = request.getParameter(AuthConfig.ACCESS_TOKEN);
		if (StringUtils.isBlank(bearerToken)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			bearerToken = resolveTokenFromUser(username, password);
		}
		return bearerToken;
	}

	private String resolveTokenFromUser(String username, String password) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			userService.lastLoginUserModify(username, WebUtils.getClientIP());
		} catch (Exception ex) {
			if(ex instanceof BadCredentialsException) {
				throw new IllegalArgumentException("用户账号或密码错误！", ex);
			}
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}

		return tokenManager.createToken(username);
	}

}
