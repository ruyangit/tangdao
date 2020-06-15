/**
 *
 */
package com.tangdao.core.mybatis;

import java.io.Serializable;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
public interface IModel<TKey extends Serializable> extends Serializable {

	/**
	 * TODO 获取id
	 * 
	 * @return
	 */
	TKey getId();

	/**
	 * TODO 设置id
	 * 
	 * @param id
	 */
	void setId(TKey id);
}
