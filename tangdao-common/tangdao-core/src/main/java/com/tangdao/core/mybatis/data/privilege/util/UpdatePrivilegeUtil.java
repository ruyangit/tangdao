package com.tangdao.core.mybatis.data.privilege.util;

import java.util.ArrayList;
import java.util.List;

import com.tangdao.core.mybatis.data.privilege.factory.JoinFactory;
import com.tangdao.core.mybatis.data.privilege.factory.SqlMetadataFactory;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.SqlMetadata;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 
 * @ClassName: UpdatePrivilegeUtil.java
 * @author: Naughty Guo
 * @date: Mar 8, 2018
 */
public class UpdatePrivilegeUtil {

	/**
	 * get delete privilege SQL.
	 *
	 * @param dataPrivilegeParameter
	 * @param statement
	 * @return
	 * @return String
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static String getPrivilegeSql(DataPrivilegeParameter dataPrivilegeParameter, Statement statement) {
		SqlMetadata sqlMetadata = SqlMetadataFactory.createSqlMetadata((Update) statement);
		assemblyWhere(sqlMetadata, dataPrivilegeParameter);
		assemblyUpdateJoin(sqlMetadata, dataPrivilegeParameter);
		return sqlMetadata.getUpdate().toString();
	}
	
	/**
	 * assembly update join condition.
	 * 
	 * @param sqlMetadata
	 * @param dataPrivilegeParameter
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	private static void assemblyUpdateJoin(SqlMetadata sqlMetadata, DataPrivilegeParameter dataPrivilegeParameter) {
		Update update = sqlMetadata.getUpdate();
		List<Join> joins = update.getJoins();
		if (null == joins) {
			joins = new ArrayList<Join>();
			update.setJoins(joins);
		}
		joins.addAll(JoinFactory.createSimpleJoins(dataPrivilegeParameter));
		update.setJoins(joins);
	}
	
	/**
	 * assembly where condition.
	 * 
	 * @param sqlMetadata
	 * @param dataPrivilegeParameter
	 * @author Naughty Guo 2016年7月11日
	 */
	private static void assemblyWhere(SqlMetadata sqlMetadata, DataPrivilegeParameter dataPrivilegeParameter) {
		Table table = sqlMetadata.getTable();
//		List<Table> tables = sqlMetadata.getTables();
		Update update = sqlMetadata.getUpdate();
		Expression whereExpression = update.getWhere();
//		if (null != tables) {
//			for (Table table : tables) {
//				whereExpression = CommonPrivilegeUtil.assemblyWhere(dataPrivilegeParameter, table, whereExpression);
//				update.setWhere(whereExpression);
//			}
//		}
		if (null != table) {
			whereExpression = CommonPrivilegeUtil.assemblyWhere(dataPrivilegeParameter, table, whereExpression);
			update.setWhere(whereExpression);
		}
	}
}
