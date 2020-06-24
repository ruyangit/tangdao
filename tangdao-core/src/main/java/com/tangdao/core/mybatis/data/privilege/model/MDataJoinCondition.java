package com.tangdao.core.mybatis.data.privilege.model;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;

/**
 * 
 * @ClassName: MDataJoinCondition.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinCondition {

	private MDataColumn[] columns;
	private CombineType combineType;

	public MDataColumn[] getColumns() {
		return columns;
	}

	public void setColumns(MDataColumn[] columns) {
		this.columns = columns;
	}

	public CombineType getCombineType() {
		return combineType;
	}

	public void setCombineType(CombineType combineType) {
		this.combineType = combineType;
	}
}
