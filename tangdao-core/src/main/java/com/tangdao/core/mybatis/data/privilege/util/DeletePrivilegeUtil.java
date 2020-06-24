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
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Join;

/**
 * 
 * @ClassName: DeletePrivilegeUtil.java
 * @author: Naughty Guo
 * @date: Mar 8, 2018
 */
public class DeletePrivilegeUtil {

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
		SqlMetadata sqlMetadata = SqlMetadataFactory.createSqlMetadata((Delete) statement);
		assemblyWhere(sqlMetadata, dataPrivilegeParameter);
		assemblyDeleteJoin(sqlMetadata, dataPrivilegeParameter);
		return sqlMetadata.getDelete().toString();
	}
	
	/**
	 * assembly delete join condition.
	 * 
	 * @param sqlMetadata
	 * @param dataPrivilegeParameter
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	private static void assemblyDeleteJoin(SqlMetadata sqlMetadata, DataPrivilegeParameter dataPrivilegeParameter) {
		Delete delete = sqlMetadata.getDelete();
		List<Join> joins = delete.getJoins();
		if (null == joins) {
			joins = new ArrayList<Join>();
			delete.setJoins(joins);
		}
		joins.addAll(JoinFactory.createSimpleJoins(dataPrivilegeParameter));
		delete.setJoins(joins);
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
		Delete delete = sqlMetadata.getDelete();
		Expression whereExpression = delete.getWhere();
		whereExpression = CommonPrivilegeUtil.assemblyWhere(dataPrivilegeParameter, table, whereExpression);
		delete.setWhere(whereExpression);
	}
}
