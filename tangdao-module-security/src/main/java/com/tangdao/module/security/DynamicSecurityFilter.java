/**
 * 
 */
package com.tangdao.module.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.tangdao.module.security.exception.SecurityException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tangdao.framework.protocol.Result;
import com.tangdao.module.security.utils.TokenUtils;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class DynamicSecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	private final HttpMessageConverter<String> messageConverter;

	private final ObjectMapper mapper;

	public DynamicSecurityFilter(HttpMessageConverter<String> messageConverter, ObjectMapper mapper) {
		this.messageConverter = messageConverter;
		this.mapper = mapper;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = tokenUtils.getJwtFromRequest(request);
		if (StrUtil.isNotBlank(token)) {
			ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
			try {
				String username = tokenUtils.getUsernameFromJWT(token);

				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				
				List<String> roles = new ArrayList<String>();
				roles.add("admin");
				Set<SimpleGrantedAuthority> collect = roles.stream()
						.map(r -> new SimpleGrantedAuthority("ROLE_" + r.toUpperCase())).collect(Collectors.toSet());
				collect.add(new SimpleGrantedAuthority("core:user:*"));
				collect.add(new SimpleGrantedAuthority("core:role:list"));
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, collect);
//						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} catch (SecurityException e) {
				outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
				messageConverter.write(mapper.writeValueAsString(Result.createResult(e.getStatus())),
						MediaType.APPLICATION_JSON, outputMessage);
				return;
			} finally {
				outputMessage.close();
			}
		}

		filterChain.doFilter(request, response);
	}

}
