/**
 *
 */
package com.tangdao.core.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
@Getter
@Setter
public class TSession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Serializable id;

	private Serializable name;

	private Serializable tenant;

	private Map<String, Object> claims;

	public TSession() {
		claims = new HashMap<>();
	}

	/**
	 *  TODO 是否包含claim信息
	 * 
	 * @param key Key
	 * @return
	 */
	public boolean hasClaim(String key) {
		if (StringUtils.isBlank(key))
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
		if (StringUtils.isBlank(key))
			return null;
		return this.claims.get(key);
	}
}
