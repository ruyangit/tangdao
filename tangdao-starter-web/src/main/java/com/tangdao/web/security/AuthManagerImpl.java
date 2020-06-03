/**
 *
 */
package com.tangdao.web.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tangdao.common.constant.ErrorApiCode;
import com.tangdao.core.auth.AccessException;
import com.tangdao.core.auth.AuthManager;
import com.tangdao.core.auth.Permission;
import com.tangdao.core.auth.User;
import com.tangdao.modules.user.service.RoleService;
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
public class AuthManagerImpl implements AuthManager {

	private static final String TOKEN_PREFIX = "Bearer ";
	
	private static final String ACCESS_TOKEN = "access_token";

	@Autowired
	private JwtTokenManager tokenManager;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RoleService roleService;
	
	@Value("${user.superAdmin:ruyang}")
	private String superAdmin;

	@Override
	public User login(Object request) throws AccessException {
		HttpServletRequest req = (HttpServletRequest) request;
		String token = resolveToken(req);
		if (StringUtils.isBlank(token)) {
			throw new AccessException(ErrorApiCode.AuthFailure_UserNotFound);
		}

		try {
			tokenManager.validateToken(token);
		} catch (ExpiredJwtException e) {
			throw new AccessException(ErrorApiCode.AuthFailure_TokenExpire);
		} catch (Exception e) {
			throw new AccessException(ErrorApiCode.AuthFailure_TokenFailure);
		}

		Authentication authentication = tokenManager.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String username = authentication.getName();
		SecurityUser user = new SecurityUser();
		user.setUsername(username);
		user.setToken(token);
		user.setSuperAdmin(superAdmin.equals(username));
		return user;
	}

	@Override
	public void auth(Permission permission, User user) throws AccessException {
		if (!roleService.hasPermission(user.getUsername(), permission)) {
			throw new AccessException(ErrorApiCode.AuthFailure_Forbidden);
		}
	}

	/**
	 * Get token from header
	 */
	private String resolveToken(HttpServletRequest request) throws AccessException {
		String bearerToken = request.getHeader(AuthConfig.AUTHORIZATION_HEADER);
		if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}
		bearerToken = request.getParameter(ACCESS_TOKEN);
		if (StringUtils.isBlank(bearerToken)) {
			String userName = request.getParameter("username");
			String password = request.getParameter("password");
			bearerToken = resolveTokenFromUser(userName, password);
		}
		return bearerToken;
	}

	private String resolveTokenFromUser(String userName, String rawPassword) throws AccessException {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName, rawPassword);
			authenticationManager.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			throw new AccessException(ErrorApiCode.AuthFailure_Unauthorized);
		}

		return tokenManager.createToken(userName);
	}

}
