package com.tangdao.core.mybatis.data.privilege.model;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ColumnType;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.OperationType;

/**
 * 
 * @ClassName: MDataColumn.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataColumn {

	private MDataObject object;
	private String name;
	private String categoryKey;
	private String value;
//	private String wrapper;
	private ColumnType columnType;
	private OperationType operationType;

	public MDataObject getObject() {
		return object;
	}

	public void setObject(MDataObject object) {
		this.object = object;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryKey() {
		return categoryKey;
	}

	public void setCategoryKey(String categoryKey) {
		this.categoryKey = categoryKey;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

//	public String getWrapper() {
//		return wrapper;
//	}
//
//	public void setWrapper(String wrapper) {
//		this.wrapper = wrapper;
//	}

	public ColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(ColumnType columnType) {
		this.columnType = columnType;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	public void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}
}
