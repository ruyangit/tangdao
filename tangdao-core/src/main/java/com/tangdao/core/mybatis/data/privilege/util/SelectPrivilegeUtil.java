package com.tangdao.core.mybatis.data.privilege.util;

import java.util.ArrayList;
import java.util.List;

import com.tangdao.core.mybatis.data.privilege.factory.JoinFactory;
import com.tangdao.core.mybatis.data.privilege.factory.SqlMetadataFactory;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinRelation;
import com.tangdao.core.mybatis.data.privilege.model.SqlMetadata;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * 
 * @ClassName: SelectPrivilegeUtil.java
 * @author: Naughty Guo
 * @date: Mar 8, 2018
 */
public class SelectPrivilegeUtil {

	/**
	 * get select privilege SQL.
	 *
	 * @param dataPrivilegeParameter
	 * @param statement
	 * @return
	 * @return String
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static String getPrivilegeSql(DataPrivilegeParameter dataPrivilegeParameter, Statement statement) {
		SelectBody selectBody = null;
		SqlMetadata sqlMetadata = null;
		selectBody = ((Select) statement).getSelectBody();
		CommonPrivilegeUtil.removeIllegalJoins(selectBody, dataPrivilegeParameter);
		if (selectBody instanceof PlainSelect) {
			sqlMetadata = SqlMetadataFactory.createSqlMetadata(selectBody);
			assemblySelectJoin((PlainSelect) selectBody, dataPrivilegeParameter);
			assemblySelectJoinWhere((PlainSelect) selectBody, dataPrivilegeParameter);
			assemblyWhere(sqlMetadata, dataPrivilegeParameter);
		} else if (selectBody instanceof SetOperationList) {
			List<PlainSelect> plainSelects = new ArrayList<PlainSelect>();
			getPlainSelects((SetOperationList) selectBody, plainSelects);
			for (PlainSelect plainSelect : plainSelects) {
				sqlMetadata = SqlMetadataFactory.createSqlMetadata(plainSelect);
				assemblySelectJoin(plainSelect, dataPrivilegeParameter);
				assemblySelectJoinWhere(plainSelect, dataPrivilegeParameter);
				assemblyWhere(sqlMetadata, dataPrivilegeParameter);
			}
		}
		return selectBody.toString();
	}
	
	/**
	 * get all plain selects.
	 * 
	 * @param setOperationList
	 * @param plainSelects
	 * void
	 * @author Naughty Guo 2016年7月13日
	 */
	private static void getPlainSelects(SetOperationList setOperationList, List<PlainSelect> plainSelects) {
		for (SelectBody selectBody : setOperationList.getSelects()) {
			if (selectBody instanceof PlainSelect) {
				plainSelects.add((PlainSelect) selectBody);
			} else if (selectBody instanceof SetOperationList) {
				getPlainSelects((SetOperationList) selectBody, plainSelects);
			}
		}
	}
	
	/**
	 * assembly select join condition.
	 * 
	 * @param plainSelect
	 * @param dataPrivilegeParameter
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	private static void assemblySelectJoin(PlainSelect plainSelect, DataPrivilegeParameter dataPrivilegeParameter) {
		List<Join> joins = null;
		MDataJoinRelation[] relations = dataPrivilegeParameter.getMDataPrivilege().getRelations();
		if (null != relations && relations.length > 0) {
			SqlMetadata sqlMetadata = SqlMetadataFactory.createSqlMetadata(plainSelect);
			if (null == sqlMetadata) {
				return;
			}
			joins = JoinFactory.createJions(sqlMetadata, dataPrivilegeParameter);
			if (null != joins) {
				sqlMetadata.getPlainSelect().setJoins(joins);
			}
		}
	}
	
	/**
	 * assembly select join where condition.
	 * 
	 * @param plainSelect
	 * @param dataPrivilegeParameter
	 * @author Naughty Guo 2016年7月11日
	 */
	private static void assemblySelectJoinWhere(PlainSelect plainSelect, DataPrivilegeParameter dataPrivilegeParameter) {
		SelectBody selectBody = null;
		SqlMetadata subSqlMetadata = null;
		List<Join> joins = plainSelect.getJoins();
		if (null != joins && joins.size() > 0) {
			for (Join join : joins) {
				if (join.getRightItem() instanceof SubSelect) {
					selectBody = ((SubSelect) join.getRightItem()).getSelectBody();
					subSqlMetadata = SqlMetadataFactory.createSqlMetadata(selectBody);
					assemblyWhere(subSqlMetadata, dataPrivilegeParameter);
				}
			}
		}
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
		PlainSelect plainSelect = sqlMetadata.getPlainSelect();
		Expression whereExpression = plainSelect.getWhere();
		whereExpression = CommonPrivilegeUtil.assemblyWhere(dataPrivilegeParameter, table, whereExpression);
		plainSelect.setWhere(whereExpression);
	}
}
