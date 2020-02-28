/**
 * 
 */
package com.tangdao.module.security.model;

/**
 * <p>
 * TODO 权限效力
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public enum AssertionEffect {

	/**
	 * 允许
	 */
	ALLOW,
	
	/**
	 * 拒绝（优先）
	 */
    DENY;
	
	public static AssertionEffect fromString(String v) {
        for (AssertionEffect e : values()) {
            if (e.toString().equals(v)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Invalid string representation for AssertionEffect: " + v);
    }
}
