package com.tangdao.core.mybatis.data.privilege.model;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;

/**
 * 
 * @ClassName: MDataCondition.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataCondition {

	private MDataObject reference;
	private MDataColumn[] columns;
	private String categoryGroup;
	private CombineType groupCombineType;
	private CombineType combineType;

	public MDataObject getReference() {
		return reference;
	}

	public void setReference(MDataObject reference) {
		this.reference = reference;
	}

	public MDataColumn[] getColumns() {
		return columns;
	}

	public void setColumns(MDataColumn[] columns) {
		this.columns = columns;
	}

	public String getCategoryGroup() {
		return categoryGroup;
	}

	public void setCategoryGroup(String categoryGroup) {
		this.categoryGroup = categoryGroup;
	}

	public CombineType getGroupCombineType() {
		return groupCombineType;
	}

	public void setGroupCombineType(CombineType groupCombineType) {
		this.groupCombineType = groupCombineType;
	}

	public CombineType getCombineType() {
		return combineType;
	}

	public void setCombineType(CombineType combineType) {
		this.combineType = combineType;
	}
}
