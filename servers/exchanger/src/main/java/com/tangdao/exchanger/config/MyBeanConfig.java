/**
 *
 */
package com.tangdao.exchanger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tangdao.core.service.HostWhitelistService;
import com.tangdao.core.service.SmsApiFailedRecordService;
import com.tangdao.core.service.UserBalanceLogService;
import com.tangdao.core.service.UserBalanceService;
import com.tangdao.core.service.UserDeveloperService;
import com.tangdao.core.service.UserSmsConfigService;

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
	public UserBalanceService userBalanceService() {
		return new UserBalanceService();
	}
	
	@Bean
	public UserDeveloperService userDeveloperService() {
		return new UserDeveloperService();
	}
	
	@Bean
	public HostWhitelistService hostWhitelistService() {
		return new HostWhitelistService();
	}
	
	@Bean
	public UserSmsConfigService userSmsConfigService() {
		return new UserSmsConfigService();
	}

	@Bean
	public SmsApiFailedRecordService smsApiFailedRecordService() {
		return new SmsApiFailedRecordService();
	}

	@Bean
	public UserBalanceLogService userBalanceLogService() {
		return new UserBalanceLogService();
	}
}
