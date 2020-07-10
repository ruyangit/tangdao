/**
 *
 */
package com.tangdao.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.tangdao.modules.sys.service.CacheService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月10日
 */
@Configuration
@EnableScheduling
public class ScheduledConfig {

	@Autowired
	private CacheService cacheService;

	@Scheduled(cron = "0 0 0/1 * * *")
	public void clearAll() {
		cacheService.clearAll();
	}
}
