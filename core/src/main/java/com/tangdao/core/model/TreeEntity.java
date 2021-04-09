/**
 *
 */
package com.tangdao.core.model;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tangdao.core.utils.BeanUtil;

import cn.hutool.core.util.StrUtil;
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

	public String pid;

	public String pids;

	public Integer treeSort;

	public String treeNames;

	private String status;

	@TableField(exist = false)
	public List<T> children;

	public void addChild(T node) {
		this.children.add(node);
	}

	@JsonIgnore
	@TableField(exist = false)
	private String treeNameKey;

	@JsonIgnore
	@TableField(exist = false)
	private String treeNameVal;

	public String getTreeNameKey() {
		if (StrUtil.isBlank(treeNameKey)) {
			this.treeNameKey = BeanUtil.getTreeNameKey(this.getClass());
		}
		return this.treeNameKey;
	}

	public String getTreeNameVal() {
		if (StrUtil.isBlank(treeNameVal)) {
			this.treeNameVal = StrUtil.toString(BeanUtil.getFieldValue(this, getTreeNameKey()));
		}
		return this.treeNameVal;
	}
}
