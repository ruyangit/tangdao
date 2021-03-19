/**
 *
 */
package com.tangdao.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tangdao.core.service.LogService;
import com.tangdao.core.service.RoleService;
import com.tangdao.core.service.UserService;
import com.tangdao.core.web.SpringUtils;
import com.tangdao.core.web.interceptor.SqlCommitTypeInterceptor;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Configuration
public class MyBeanConfig {

	// @Component
	@Bean
	public SpringUtils springUtils() {
		return new SpringUtils();
	}

	@Bean
	public SqlCommitTypeInterceptor sqlCommitTypeInterceptor() {
		return new SqlCommitTypeInterceptor();
	}

	// @Service
	@Bean
	public UserService userService() {
		return new UserService();
	}

	@Bean
	public RoleService roleService() {
		return new RoleService();
	}

	@Bean
	public LogService logService() {
		return new LogService();
	}
}
