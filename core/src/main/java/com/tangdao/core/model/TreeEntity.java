/**
 *
 */
package com.tangdao.core.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月1日
 */
@Getter
@Setter
public abstract class TreeEntity<T> extends DataEntity<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String ROOT_ID = "0";

	public String pid;

	public String pids;

	public Integer treeSort;

	public String treeNames;

	@TableField(exist = false)
	public List<T> children;

	public void addChild(T node) {
		this.children.add(node);
	}

}
