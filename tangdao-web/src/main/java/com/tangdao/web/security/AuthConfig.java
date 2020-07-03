/**
 *
 */
package com.tangdao.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

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
@EnableWebSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String ACCESS_TOKEN = "access_token";

	@Autowired
	private JwtTokenManager tokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
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
			.and()
				.authorizeRequests().antMatchers("/admin/**").authenticated()
//				.authorizeRequests().antMatchers("/admin/**").access("@userPermissionService.hasPermission(authentication, request)")
			.and()
				.exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint());

		// disable cache
		http.headers().cacheControl();
		// jwt filter
		http.addFilterBefore(new JwtAuthenticationTokenFilter(tokenProvider),
				UsernamePasswordAuthenticationFilter.class);
	}
	
}
