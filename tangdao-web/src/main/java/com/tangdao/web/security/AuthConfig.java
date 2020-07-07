/**
 *
 */
package com.tangdao.web.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.access.vote.UnanimousBased;
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
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.modules.sys.service.PolicyService;
import com.tangdao.web.security.filter.JwtAuthenticationTokenFilter;
import com.tangdao.web.security.user.UserDetailsServiceImpl;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Configuration
public class AuthConfig extends WebSecurityConfigurerAdapter {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String ACCESS_TOKEN = "access_token";

	@Autowired
	private JwtTokenManager tokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private PolicyService policyService;
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public JwtAuthenticationTokenFilter jwtAuthTokenFilter() {
		return new JwtAuthenticationTokenFilter(tokenProvider);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable().cors() // We don't need CSRF for JWT based authentication
			.and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers("/admin/login").permitAll()
				.antMatchers("/admin/**").authenticated()
				.accessDecisionManager(accessDecisionManager())
			.and()
				.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
					
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
						// TODO Auto-generated method stub
						WebUtils.responseJson(response, CommonApiCode.UNAUTHORIZED);
					}
				}).accessDeniedHandler(new AccessDeniedHandler() {
					
					@Override
					public void handle(HttpServletRequest request, HttpServletResponse response,
							AccessDeniedException accessDeniedException) throws IOException, ServletException {
						// TODO Auto-generated method stub
						WebUtils.responseJson(response, CommonApiCode.FORBIDDEN);
					}
				});

		http.headers().cacheControl();
		// jwt filter
		http.addFilterBefore(jwtAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Bean
	public AccessDecisionManager accessDecisionManager() {
	    List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(
	        new WebExpressionVoter(),
	        new RoleVoter(),
	        new AuthenticatedVoter(),
	        new PoliciesVoter(policyService));
	    return new UnanimousBased(decisionVoters);
	}
	
}
