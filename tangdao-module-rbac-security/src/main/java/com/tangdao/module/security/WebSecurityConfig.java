/**
 * 
 */
package com.tangdao.module.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * <p>
 * TODO 基于 Rbac 权限控制
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
	
	public static final String[] DOCS_INFRA_API = {
            "/swagger-resources/**", 
            "//swagger-resources/configuration/**", 
            "/swagger-ui.html", 
            "/swagger-ui.html/**",
            "/v2/api-docs", 
            "/webjars/**", 
            "/actuator/**",
            "/**.html",
            "/configuration/**"};

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		
		@Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	    	return super.authenticationManagerBean();
	    }
	    
	    @Override
	 	public void configure(WebSecurity web) {
	 		web.ignoring()
	 			.antMatchers(HttpMethod.GET, "/")
	 			.antMatchers(DOCS_INFRA_API);
	 	}
	    
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.authenticationProvider(daoAuthenticationProvider());
	    }

	    @Bean
	    public DaoAuthenticationProvider daoAuthenticationProvider(){
	        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	        // 设置userDetailsService
	        provider.setUserDetailsService(userDetailsService);
	        // 禁止隐藏用户未找到异常
	        provider.setHideUserNotFoundExceptions(false);
	        // 使用BCrypt进行密码的hash
	        provider.setPasswordEncoder(passwordEncoder());
	        return provider;
	    }
	}
}
