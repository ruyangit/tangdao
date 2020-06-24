package com.tangdao.core.mybatis.data.privilege.factory;

import org.apache.commons.lang3.StringUtils;

import com.tangdao.core.mybatis.data.privilege.model.MDataObject;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Table;

/**
 * TableFactory
 * 
 * @ClassName: 
 * @author: Naughty Guo
 * @date: 2016年7月16日
 */
public class TableFactory {

	/**
	 * create table.
	 * 
	 * @param tableName
	 * @param aliasName
	 * @return
	 * Table
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Table createTable(String tableName, String aliasName) {
		Alias alias = null;
		Table table = new Table();
		if (StringUtils.isBlank(tableName)) {
			tableName = "";
		}
		if (StringUtils.isNotBlank(aliasName)) {
			alias = new Alias(aliasName);
			alias.setUseAs(false);
		}
		table.setName(tableName);
		table.setAlias(alias);
		return table;
	}
	
	/**
	 * create table.
	 * 
	 * @param mDataObject
	 * @return
	 * Table
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Table createTable(MDataObject mDataObject) {
		return createTable(mDataObject.getName(), mDataObject.getAlias());
	}
	
	/**
	 * create table.
	 * 
	 * @param tableName
	 * @return
	 * Table
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Table createTable(String tableName) {
		return createTable(tableName, null);
	}
}
