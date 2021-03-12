/**
 *
 */
package com.tangdao.portal.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.utils.ServletUtil;
import com.tangdao.portal.web.security.service.IUserDetailsService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String ACCESS_TOKEN = "access_token";

	public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";
	
	@Autowired
	private IUserDetailsService userDetailsService;
	
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable().cors() // We don't need CSRF for JWT based authentication
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers("/api/**").permitAll()
				.antMatchers("/**").authenticated()
			.and()
				.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler());
	
		http.headers().frameOptions().disable();
		http.headers().cacheControl();
		// jwt filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	public AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				ServletUtil.responseJson(response, CommonApiCode.COMMON_AUTHENTICATION_FAILED);
			}
		};
	}
	
	public AccessDeniedHandler accessDeniedHandler() {
		return new AccessDeniedHandler() {
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				// TODO Auto-generated method stub
				ServletUtil.responseJson(response, CommonApiCode.COMMON_ACCESS_DENIED);
			}
		};
	}
}
