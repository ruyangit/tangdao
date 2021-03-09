/**
 *
 */
package com.tangdao.core;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
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
	 * 
	 * @param node
	 */
	public void addChild(T node) {
		this.children.add(node);
	}

	/**
	 * 
	 * TODO 是否根节点
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return ROOT_CODE.equals(getParentCode());
	}

	/**
	 * 
	 * TODO 是否叶子节点
	 * 
	 * @return
	 */
	public boolean isLeaf() {
		return CollUtil.isEmpty(children);
	}

	/**
	 * 获取树节点名字
	 * 
	 * @param isShowCode 是否显示编码<br>
	 *                   true or 1：显示在左侧：(code)name<br>
	 *                   2：显示在右侧：name(code)<br>
	 *                   false or null：不显示编码：name
	 * @param code       编码
	 * @param name       名称
	 * @return
	 */
	public String getTreeNodeName(String isShowCode, String code, String name) {
		if ("true".equals(isShowCode) || "1".equals(isShowCode)) {
			return "(" + code + ") " + StrUtil.replace(name, " ", "");
		} else if ("2".equals(isShowCode)) {
			return StrUtil.replace(name, " ", "") + " (" + code + ")";
		} else {
			return StrUtil.replace(name, " ", "");
		}
	}

}
