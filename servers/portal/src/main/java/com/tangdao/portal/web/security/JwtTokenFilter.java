/**
 *
 */
package com.tangdao.portal.web.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.utils.ServletUtil;

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
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String token = resolveToken(request);
			if (StrUtil.isNotBlank(token) && !isAuthenticated()) {
				jwtTokenProvider.validateToken(token);
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			ServletUtil.responseJson(response, CommonApiCode.COMMON_REQUEST_TIMESTAMPS_EXPIRED);
			return;
		} catch (Exception e) {
			ServletUtil.responseJson(response, CommonApiCode.COMMON_AUTHENTICATION_FAILED);
			return;
		}

	}

	/**
	 * Get token from header
	 */
	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(WebSecurityConfig.AUTHORIZATION_HEADER);
		if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith(WebSecurityConfig.TOKEN_PREFIX)) {
			return bearerToken.substring(7);
		}
		String jwt = request.getParameter(WebSecurityConfig.ACCESS_TOKEN);
		if (StrUtil.isNotBlank(jwt)) {
			return jwt;
		}
		return null;
	}

	/**
	 * 是否已經認證
	 * 
	 * @return
	 */
	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return true;
	}

}
