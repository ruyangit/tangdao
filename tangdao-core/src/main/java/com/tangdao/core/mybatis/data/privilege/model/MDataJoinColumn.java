package com.tangdao.core.mybatis.data.privilege.model;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.OperationType;

/**
 * 
 * @ClassName: MDataJoinColumn.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinColumn {

	private String masterColumn;
	private String slaveColumn;
	private OperationType operationType;

	public String getMasterColumn() {
		return masterColumn;
	}

	public void setMasterColumn(String masterColumn) {
		this.masterColumn = masterColumn;
	}

	public String getSlaveColumn() {
		return slaveColumn;
	}

	public void setSlaveColumn(String slaveColumn) {
		this.slaveColumn = slaveColumn;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}
}
