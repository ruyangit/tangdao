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
import com.tangdao.core.session.SessionContext;
import com.tangdao.core.session.TSession;
import com.tangdao.core.web.aspect.LogUtils;
import com.tangdao.modules.sys.service.UserService;
import com.tangdao.web.security.user.SecurityUser;
import com.tangdao.web.security.user.SecurityUserDetails;

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

	@Autowired
	private LogUtils logUtils;

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
		return (SecurityUser) authentication.getPrincipal();
	}

	/**
	 * Get token from header
	 */
	public String resolveToken(HttpServletRequest request) {
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
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
					password);
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			userService.lastLoginUserModify(username, WebUtils.getClientIP());
			
			// 认证信息
			SecurityUserDetails securityUserDetails = (SecurityUserDetails) authentication.getPrincipal();
			
			TSession session = new TSession();
			session.setUsername(securityUserDetails.getUsername());
			session.setUserId(securityUserDetails.getSecurityUser().getId());
			SessionContext.setSession(session);
			// 保存登录日志
			logUtils.saveLog("用户认证", "用户【" + username + "】登录，IP地址：" + WebUtils.getClientIP() + "。");
			return tokenManager.createToken(authentication);
		} catch (Exception ex) {
			if (ex instanceof BadCredentialsException) {
				throw new IllegalArgumentException("用户账号或密码错误！", ex);
			}
			throw new IllegalArgumentException(ex.getMessage(), ex);
		} finally {
			SessionContext.removeSession();
		}

	}

}
