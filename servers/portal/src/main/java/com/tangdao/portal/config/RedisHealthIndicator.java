/**
 *
 */
package com.tangdao.portal.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 替代原先默认的检查逻辑
 * </p>
 *
 * @author ruyang
 * @since 2020年11月19日
 */
@Component
public class RedisHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		// TODO Auto-generated method stub
		return Health.up().build();
	}

}
