package com.tangdao.core.mybatis.data.privilege.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.mybatis.data.privilege.model.SqlMetadata;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;

/**
 * SqlMetadataFactory
 * 
 * @ClassName: 
 * @author: Naughty Guo
 * @date: 2016年7月12日
 */
public class SqlMetadataFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(SqlMetadataFactory.class);
	
	/**
	 * create select metadata.
	 * 
	 * @param selectBody
	 * @return
	 * Table
	 * @author Naughty Guo 2016年7月13日
	 */
	public static SqlMetadata createSqlMetadata(SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			FromItem fromItem = ((PlainSelect) selectBody).getFromItem();
			if (fromItem instanceof Table) {
				return new SqlMetadata((Table) fromItem, (PlainSelect) selectBody);
			} else if (fromItem instanceof SubSelect) {
				SubSelect subSelect = (SubSelect) fromItem;
				return createSqlMetadata(subSelect.getSelectBody());
			}
		}
		LOGGER.error("no reference sql metadata find.");
		return null;
	}
	
	/**
	 * create delete metadata
	 *
	 * @param delete
	 * @return
	 * @return SqlMetadata
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static SqlMetadata createSqlMetadata(Delete delete) {
		return new SqlMetadata(delete);
	}
	
	/**
	 * create update metadata
	 *
	 * @param update
	 * @return
	 * @return SqlMetadata
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static SqlMetadata createSqlMetadata(Update update) {
		return new SqlMetadata(update);
	}
}
