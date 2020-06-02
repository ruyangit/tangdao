/**
 *
 */
package com.tangdao.web.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.tangdao.core.auth.AuthFilter;
import com.tangdao.core.code.ControllerMethodsCache;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Configuration
@EnableScheduling
public class TangdaoConfig {

	@Autowired
	private ControllerMethodsCache methodsCache;

	@PostConstruct
	public void init() {
		methodsCache.initClassMethod("com.tangdao.web.controller");
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.setMaxAge(18000L);
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public FilterRegistrationBean<AuthFilter> authFilterRegistration() {
		FilterRegistrationBean<AuthFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(authFilter());
		registration.addUrlPatterns("/*");
		registration.setName("authFilter");
		registration.setOrder(6);

		return registration;
	}

	@Bean
	public AuthFilter authFilter() {
		return new AuthFilter();
	}
}
