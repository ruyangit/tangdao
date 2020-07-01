/**
 *
 */
package com.tangdao.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

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
	private String name;
	
	@Override
	public Health health() {
		// TODO Auto-generated method stub
		return Health.up().withDetail("name", name).build();
	}

}
