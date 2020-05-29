/**
 *
 */
package com.tangdao.core.auth;

import java.io.Serializable;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public class Resource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SPLITTER = ":";

	public static final String ANY = "*";

	/**
	 * The unique key of resource.
	 */
	private String key;

	public Resource(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public String parseName() {
		return key.substring(0, key.lastIndexOf(SPLITTER));
	}

	@Override
	public String toString() {
		return "Resource{" + "key='" + key + '\'' + '}';
	}
}
