/**
 *
 */
package com.tangdao.core.auth;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum ActionTypes {
	
	/**
	 * Read
	 */
	READ("r"),
	/**
	 * Write
	 */
	WRITE("w");

	private String action;

	ActionTypes(String action) {
		this.action = action;
	}

	@Override
	public String toString() {
		return action;
	}
}
