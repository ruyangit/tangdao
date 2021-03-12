/**
 *
 */
package com.tangdao.portal.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;
import com.tangdao.portal.web.security.model.AuthUser;

import cn.hutool.core.util.StrUtil;
import io.jsonwebtoken.ExpiredJwtException;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@Component
public class AuthProvider {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	public AuthUser login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = resolveToken(request);
		try {
			jwtTokenProvider.validateToken(token);
		} catch (ExpiredJwtException ex) {
			throw new BusinessException(CommonApiCode.COMMON_REQUEST_TIMESTAMPS_EXPIRED);
		} catch (Exception ex) {
			throw new BusinessException(CommonApiCode.COMMON_AUTHENTICATION_FAILED);
		}

		Authentication authentication = jwtTokenProvider.getAuthentication(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return (AuthUser) authentication.getPrincipal();
	}

	/**
	 * Get token from header
	 */
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(WebSecurityConfig.AUTHORIZATION_HEADER);
		if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(WebSecurityConfig.TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}
		bearerToken = request.getParameter(WebSecurityConfig.ACCESS_TOKEN);
		if (StrUtil.isBlank(bearerToken)) {
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			bearerToken = resolveTokenFromUser(username, password);
		}
		return bearerToken;
	}

	/**
	 * 
	 * TODO
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	private String resolveTokenFromUser(String username, String password) {
		try {
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
					password);
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			return jwtTokenProvider.createToken(authentication);
		} catch (Exception ex) {
			if (ex instanceof InternalAuthenticationServiceException) {
				throw new BusinessException(CommonApiCode.COMMON_APPKEY_INVALID);
			}
			throw new BusinessException(CommonApiCode.COMMON_AUTHENTICATION_FAILED);
		}

	}

}
