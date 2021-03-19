/**
 *
 */
package com.tangdao.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tangdao.core.service.RoleService;
import com.tangdao.core.service.UserService;

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

	@Bean
	public UserService userService() {
		return new UserService();
	}

	@Bean
	public RoleService roleService() {
		return new RoleService();
	}
}
