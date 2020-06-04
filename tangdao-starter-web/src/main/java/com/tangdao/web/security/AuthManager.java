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

import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.exception.BusinessException;
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
	private AuthenticationManager authenticationManager;

	@Value("${user.superAdmin:ruyang}")
	private String superAdmin;

	public SecurityUser login(HttpServletRequest request) {
		String token = resolveToken(request);
		try {
			tokenManager.validateToken(token);
		} catch (ExpiredJwtException e) {
			throw new BusinessException(CommonApiCode.USER_TOKEN_EXPIRE);
		} catch (Exception e) {
			throw new BusinessException(CommonApiCode.UNAUTHORIZED);
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
			String userName = request.getParameter("username");
			String password = request.getParameter("password");
			bearerToken = resolveTokenFromUser(userName, password);
		}
		return bearerToken;
	}

	private String resolveTokenFromUser(String userName, String rawPassword) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userName,
					rawPassword);
			authenticationManager.authenticate(authenticationToken);
		} catch (AuthenticationException e) {
			throw new IllegalArgumentException("用户名或密码输入有误");
		}

		return tokenManager.createToken(userName);
	}

}
