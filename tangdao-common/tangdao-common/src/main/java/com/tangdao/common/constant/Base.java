/**
 *
 */
package com.tangdao.common.constant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月14日
 */
public interface Base<K, V> {

	/**
	 * TODO 枚举值
	 * 
	 * @return
	 */
	public K getCode();

	/**
	 * TODO 枚举描述
	 * 
	 * @return
	 */
	public V getMessage();
}
