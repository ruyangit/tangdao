/**
 *
 */
package com.tangdao.model.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * <p>
 * TODO 会话用户
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
@Getter
@Setter
public class SessionUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String username;

	private Map<String, Object> claims;

	public SessionUser() {
		claims = new HashMap<>();
	}

	/**
	 * TODO 是否包含claim信息
	 * 
	 * @param key Key
	 * @return
	 */
	public boolean hasClaim(String key) {
		if (StrUtil.isBlank(key))
			return false;
		return this.claims.containsKey(key);
	}

	/**
	 * TODO 获取claim信息
	 * 
	 * @param key Key
	 * @return
	 */
	public Object getClaim(String key) {
		if (StrUtil.isBlank(key))
			return null;
		return this.claims.get(key);
	}
}
