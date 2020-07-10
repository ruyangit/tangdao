/**
 *
 */
package com.tangdao.modules.sys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月10日
 */
@Service
public class CacheService {

	public static final String RED_USER_MENU = "red_user_menu";
	public static final String RED_USER_POLICY_STATEMENTS = "red_user_policy_statements";

	@Autowired
	private CacheManager cacheManager;

	public void clearAll() {
		cacheManager.getCacheNames().forEach(e -> {
			cacheManager.getCache(e).clear();
		});
	}

	public void clear(String name) {
		cacheManager.getCache(name).clear();
	}

	public void clear(String name, String key) {
		cacheManager.getCache(name).evict(key);
	}
}
