/**
 *
 */
package com.tangdao.core;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月8日
 */
@Getter
@Setter
public class TreeEntity<T> extends DataEntity<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String parentCode;

	protected String parentCodes;

	protected String treeNames;

	protected Integer treeSort;
	
	// 根节点编码
	public static final String ROOT_CODE = "0";

	// 默认排序编码
	public static final int DEFAULT_TREE_SORT = 30;

}
