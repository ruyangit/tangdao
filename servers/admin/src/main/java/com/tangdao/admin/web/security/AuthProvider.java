/**
 *
 */
package com.tangdao.admin.web.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.admin.web.security.model.AuthUser;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;
import com.tangdao.core.model.domain.Log;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.service.UserService;
import com.tangdao.core.utils.LogUtil;

import cn.hutool.core.net.NetUtil;
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

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

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
			
			// 存储会话用户
			jwtTokenProvider.setSessionContext(authentication);
			
			// 更新登录用户信息
			User user = new User();
			user.setLastLoginIp(NetUtil.getLocalhostStr());
			user.setLastLoginDate(new Date());
			userService.update(user, Wrappers.<User>lambdaUpdate().eq(User::getUsername, username));

			// 写入登录日志
			LogUtil.saveLog("登录", Log.TYPE_LOGIN_LOGOUT);
			log.info("用户【" + username + "】登录，IP地址：" + user.getLastLoginIp() + ".");
			return jwtTokenProvider.createToken(authentication);
		} catch (Exception ex) {
			if (ex instanceof InternalAuthenticationServiceException) {
				throw new BusinessException(CommonApiCode.COMMON_APPKEY_INVALID);
			}
			throw new BusinessException(CommonApiCode.COMMON_AUTHENTICATION_FAILED);
		}

	}

}
