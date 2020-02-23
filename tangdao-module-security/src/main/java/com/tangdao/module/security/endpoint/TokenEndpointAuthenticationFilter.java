/**
 * 
 */
package com.tangdao.module.security.endpoint;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
public class TokenEndpointAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private final HttpMessageConverter<String> messageConverter;

    private final ObjectMapper mapper;
    
    public TokenEndpointAuthenticationFilter(ObjectMapper mapper) {
        this.messageConverter = new StringHttpMessageConverter();
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
	        	UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	        	authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        	
	        	SecurityContextHolder.getContext().setAuthentication(authentication);
        	} catch (Exception e) {
        		Result result = Result.createResult();
        		result.fail(e.getMessage());
        		
        		outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
        		messageConverter.write(mapper.writeValueAsString(result), MediaType.APPLICATION_JSON, outputMessage);
        		return;
        	} finally {
        		outputMessage.close();
        	}
        }
        
        filterChain.doFilter(request, response);
	}

}
