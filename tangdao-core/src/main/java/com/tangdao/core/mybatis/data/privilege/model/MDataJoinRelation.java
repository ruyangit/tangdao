package com.tangdao.core.mybatis.data.privilege.model;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ObjectJoinType;

/**
 * 
 * @ClassName: MDataJoinRelation.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinRelation {

	private MDataObject master;
	private MDataObject slave;
	private MDataJoinColumn[] joinColumns;
	private MDataJoinCondition[] conditions;
	private ObjectJoinType joinType;

	public MDataObject getMaster() {
		return master;
	}

	public void setMaster(MDataObject master) {
		this.master = master;
	}

	public MDataObject getSlave() {
		return slave;
	}

	public void setSlave(MDataObject slave) {
		this.slave = slave;
	}

	public MDataJoinColumn[] getJoinColumns() {
		return joinColumns;
	}

	public void setJoinColumns(MDataJoinColumn[] joinColumns) {
		this.joinColumns = joinColumns;
	}

	public MDataJoinCondition[] getConditions() {
		return conditions;
	}

	public void setConditions(MDataJoinCondition[] conditions) {
		this.conditions = conditions;
	}

	public ObjectJoinType getJoinType() {
		return joinType;
	}

	public void setJoinType(ObjectJoinType joinType) {
		this.joinType = joinType;
	}
}
