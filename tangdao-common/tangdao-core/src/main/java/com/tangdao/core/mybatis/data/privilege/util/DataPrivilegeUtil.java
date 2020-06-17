package com.tangdao.core.mybatis.data.privilege.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

/**
 * 
 * @ClassName: DataPrivilegeUtil.java
 * @author: Naughty Guo
 * @date: Jun 10, 2016
 */
public class DataPrivilegeUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataPrivilegeUtil.class);

	/**
	 * get changed SQL with data privilege condition.
	 * 
	 * @param dataPrivilegeParameter
	 * @return
	 * @return String
	 * @author Naughty Guo Jun 10, 2016
	 */
	public static String getPrivilegeSql(DataPrivilegeParameter dataPrivilegeParameter) {
		String result = null;
		try {
			String strSql = CommonPrivilegeUtil.handleSqlSegments(dataPrivilegeParameter);
			Statement statement = CCJSqlParserUtil.parse(strSql);
			if (statement instanceof Select) {
				result = SelectPrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter, statement);
			} else if (statement instanceof Delete) {
				result = DeletePrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter, statement);
			} else if (statement instanceof Update) {
				result = UpdatePrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter, statement);
			} else {
				result = dataPrivilegeParameter.getOriginalSql();
			}
		} catch (JSQLParserException e) {
			LOGGER.warn("parse SQL error.", e);
			result = dataPrivilegeParameter.getOriginalSql();
		}
		return result;
	}
}
