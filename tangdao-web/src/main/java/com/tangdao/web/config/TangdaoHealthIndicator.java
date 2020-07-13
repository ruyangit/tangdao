/**
 *
 */
package com.tangdao.web.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import com.tangdao.modules.sys.service.CacheService;

import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月1日
 */
@Component
public class TangdaoHealthIndicator implements HealthIndicator {
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired
	private CacheService cacheService;
	
	@Override
	public Health health() {
		// TODO Auto-generated method stub
		Map<String, Object> details = MapUtil.newHashMap();
		details.put("applicationName", applicationName);
		details.put("cacheNameCount", cacheService.getCacheNames().size());
		return Health.up().withDetails(details).build();
	}

}
