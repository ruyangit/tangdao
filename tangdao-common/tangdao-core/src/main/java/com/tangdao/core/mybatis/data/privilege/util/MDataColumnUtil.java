package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataColumn;
import com.tangdao.core.mybatis.data.privilege.model.MDataColumn;

/**
 * 
 * @ClassName: MDataColumnUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataColumnUtil {

	/**
	 * create MDataColumn by @DataColumn.
	 * 
	 * @param dataColumn
	 * @return
	 * @return MDataColumn
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataColumn create(DataColumn dataColumn) {
		MDataColumn mDataColumn = new MDataColumn();
		mDataColumn.setColumnType(dataColumn.columnType());
//		mDataColumn.setWrapper(dataColumn.wrapper());
		mDataColumn.setName(dataColumn.name());
		mDataColumn.setObject(MDataObjectUtil.create(dataColumn.object()));
		mDataColumn.setOperationType(dataColumn.operationType());
		mDataColumn.setValue(dataColumn.value());
		mDataColumn.setCategoryKey(dataColumn.categoryKey());
		return mDataColumn;
	}
	
	/**
	 * create MDataColumn by @DataColumn.
	 * 
	 * @param dataColumns
	 * @return
	 * @return MDataColumn[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataColumn[] create(DataColumn[] dataColumns) {
		MDataColumn[] mDataColumns = new MDataColumn[dataColumns.length];
		for (int i = 0; i < dataColumns.length; i++) {
			mDataColumns[i] = create(dataColumns[i]);
		}
		return mDataColumns;
	}
}
