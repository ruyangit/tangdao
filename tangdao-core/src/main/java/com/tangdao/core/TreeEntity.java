/**
 *
 */
package com.tangdao.core;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import cn.hutool.core.collection.CollUtil;
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
	
	// 子节点
	@TableField(exist = false)
	protected List<T> children;
	
	// 根节点编码
	public static final String ROOT_CODE = "0";

	// 默认排序编码
	public static final int DEFAULT_TREE_SORT = 30;
	
	/**
	 * 
	 * TODO 添加一个节点
	 * @param node
	 */
	public void addChild(T node) {
		this.children.add(node);
	}
	
	/**
	 * 
	 * TODO 是否根节点
	 * @return
	 */
	public boolean isRoot() {
		return ROOT_CODE.equals(getParentCode());
	}
	
	/**
	 * 
	 * TODO 是否叶子节点
	 * @return
	 */
	public boolean isLeaf() {
		return CollUtil.isEmpty(children);
	}

}
