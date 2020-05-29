/**
 *
 */
package com.tangdao.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.tangdao.web.security.AuthConfig;
import com.tangdao.web.security.JwtTokenManager;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter{

	private static final String TOKEN_PREFIX = "Bearer ";
	
	private static final String ACCESS_TOKEN = "access_token";
	
	private JwtTokenManager tokenManager;

    public JwtAuthenticationTokenFilter(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String jwt = resolveToken(request);

        if (StringUtils.isNotBlank(jwt) && SecurityContextHolder.getContext().getAuthentication() == null) {
            this.tokenManager.validateToken(jwt);
            Authentication authentication = this.tokenManager.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
	}
	
	/**
     * Get token from header
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AuthConfig.AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(ACCESS_TOKEN);
        if (StringUtils.isNotBlank(jwt)) {
            return jwt;
        }
        return null;
    }

}
