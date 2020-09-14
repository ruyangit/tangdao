/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
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

	public Collection<String> getCacheNames() {
		return cacheManager.getCacheNames();
	}

	public Cache getCache(String name) {
		return cacheManager.getCache(name);
	}

	public void clearAll() {
		getCacheNames().forEach(e -> {
			cacheManager.getCache(e).clear();
		});
	}

	public void clear(String name) {
		getCache(name).clear();
	}

	public void clear(String name, String key) {
		getCache(name).evict(key);
	}
}
