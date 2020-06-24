package com.tangdao.core.mybatis.data.privilege.model;

/**
 * 
 * @ClassName: MDataSqlSegment.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataSqlSegment {

	private String var;
	private String sql;
	private String[] values;
	private String[] categoryKeys;

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String[] getValues() {
		return values;
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String[] getCategoryKeys() {
		return categoryKeys;
	}

	public void setCategoryKeys(String[] categoryKeys) {
		this.categoryKeys = categoryKeys;
	}
}
