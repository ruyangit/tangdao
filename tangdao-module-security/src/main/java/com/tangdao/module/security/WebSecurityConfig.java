/**
 * 
 */
package com.tangdao.module.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class WebSecurityConfig {
	
//	public static final String[] DOCS_INFRA_API = {
//            "/swagger-resources/**", 
//            "//swagger-resources/configuration/**", 
//            "/swagger-ui.html", 
//            "/swagger-ui.html/**",
//            "/v2/api-docs", 
//            "/webjars/**", 
//            "/actuator/**",
//            "/**.html",
//            "/configuration/**"};

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private UserDetailsService userDetailsService;
		
		@Autowired
		private JwtAuthenticationFilter jwtAuthenticationFilter;
		
		@Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
		
		@Bean
	    @Override
	    public AuthenticationManager authenticationManagerBean() throws Exception {
	    	return super.authenticationManagerBean();
	    }
	    
//	    @Override
//	 	public void configure(WebSecurity web) {
//	 		web.ignoring()
//	 			.antMatchers(HttpMethod.GET, "/")
//	 			.antMatchers(DOCS_INFRA_API);
//	 	}
	    
	    @Override
	    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.authenticationProvider(daoAuthenticationProvider());
	    }
	    
	    @Override
	    public void configure(HttpSecurity http) throws Exception {
	        http
	        	.cors()
	        	.and()
	    		.csrf().disable()
	    		.formLogin().disable()
	    		.httpBasic().disable()
//	            .authorizeRequests()
//	            	.antMatchers(WebSecurityConfig.DOCS_INFRA_API).permitAll()
//	            	.antMatchers(HttpMethod.GET, "/auth/token").permitAll()
	    		.antMatcher("/api/**")
	    			.authorizeRequests()
	            // RBAC 动态 url 认证
	    			.anyRequest().access("@rbacAuthorityService.hasPermission(request, authentication)")
//	            .anyRequest().access("hasRole('ROLE_SUPER')")
	            .and()
	            .logout().disable()
	            .sessionManagement()
	            	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//	            .and()
//	            .exceptionHandling()
//	            	.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(new CustomAccessDeniedHandler())
	            ;
	        // 添加自定义 JWT 过滤器
	        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
