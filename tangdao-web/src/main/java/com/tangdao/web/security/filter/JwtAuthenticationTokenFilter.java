/**
 *
 */
package com.tangdao.web.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.web.security.AuthConfig;
import com.tangdao.web.security.JwtTokenManager;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

	private JwtTokenManager tokenManager;

    public JwtAuthenticationTokenFilter(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			String token = resolveToken(request);

			if (StringUtils.isNotBlank(token) && !isAuthenticated()) {
			    this.tokenManager.validateToken(token);
			    Authentication authentication = this.tokenManager.getAuthentication(token);
			    SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			WebUtils.responseJson(response, CommonApiCode.USER_TOKEN_EXPIRE);
			return;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WebUtils.responseJson(response, CommonApiCode.UNAUTHORIZED);
			return;
		}
	}
	
	/**
     * Get token from header
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConfig.AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(AuthConfig.TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(AuthConfig.ACCESS_TOKEN);
        if (StringUtils.isNotBlank(jwt)) {
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
