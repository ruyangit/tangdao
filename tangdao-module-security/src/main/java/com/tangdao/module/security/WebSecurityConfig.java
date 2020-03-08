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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tangdao.module.security.error.CustomAccessDeniedHandler;
import com.tangdao.module.security.error.CustomAuthenticationEntryPoint;

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
public class WebSecurityConfig {

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired
		private DynamicSecurityFilter dynamicSecurityFilter;

		@Autowired
		private CustomAccessDeniedHandler customAccessDeniedHandler;

		@Autowired
		private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

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
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.authenticationProvider(daoAuthenticationProvider());
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http.cors().disable().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/login/token")
					.permitAll().antMatchers("/expose/**").permitAll()
					// RBAC PBAC 动态认证
					.anyRequest().access("@dynamicAccessDecisionManager.hasPermission(request, authentication)").and()
					.logout().disable().exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint)
					.accessDeniedHandler(customAccessDeniedHandler);

			http.sessionManagement()
					// Spring Security的默认启用防止固化session攻击
					.sessionFixation().migrateSession().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

			http.headers().frameOptions().disable().cacheControl();

			// 添加自定义 JWT 过滤器
			http.addFilterBefore(dynamicSecurityFilter, UsernamePasswordAuthenticationFilter.class);
		}

		@Bean
		public DaoAuthenticationProvider daoAuthenticationProvider() {
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
