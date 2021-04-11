/**
 *
 */
package com.tangdao.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.tangdao.web.security.service.IUserDetailsService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, securedEnabled = true) // 自定义方法级安全校验
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static final String TOKEN_PREFIX = "Bearer ";

	public static final String ACCESS_TOKEN = "access_token";

	public static final String SECURITY_IGNORE_URLS_SPILT_CHAR = ",";

	@Autowired
	private IUserDetailsService userDetailsService;

	@Autowired
	private JwtTokenFilter jwtTokenFilter;

	@Autowired
	private UserAuthenticationEntryPoint userAuthenticationEntryPoint;

	@Autowired
	private UserAccessDeniedHandler userAccessDeniedHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors() // We don't need CSRF for JWT based authentication
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.antMatchers("/actuator", "/api/**").permitAll().antMatchers("/**").authenticated();

		http.exceptionHandling().authenticationEntryPoint(userAuthenticationEntryPoint)
				.accessDeniedHandler(userAccessDeniedHandler);

		http.headers().frameOptions().disable();
		http.headers().cacheControl();
		// jwt filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

	}

	@Bean
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

}
