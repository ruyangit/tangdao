package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataJoinColumn;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinColumn;

/**
 * 
 * @ClassName: MDataJoinColumnUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinColumnUtil {

	/**
	 * create MDataJoinColumn by @DataJoinColumn.
	 * 
	 * @param dataJoinColumn
	 * @return
	 * @return MDataJoinColumn
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataJoinColumn create(DataJoinColumn dataJoinColumn) {
		MDataJoinColumn mDataJoinColumn = new MDataJoinColumn();
		mDataJoinColumn.setMasterColumn(dataJoinColumn.masterColumn());
		mDataJoinColumn.setSlaveColumn(dataJoinColumn.slaveColumn());
		mDataJoinColumn.setOperationType(dataJoinColumn.operationType());
		return mDataJoinColumn;
	}
	
	/**
	 * create MDataJoinColumn by @DataJoinColumn.
	 * 
	 * @param dataJoinColumns
	 * @return
	 * @return MDataJoinColumn[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataJoinColumn[] create(DataJoinColumn[] dataJoinColumns) {
		MDataJoinColumn[] mDataJoinColumns = new MDataJoinColumn[dataJoinColumns.length];
		for (int i = 0; i < dataJoinColumns.length; i++) {
			mDataJoinColumns[i] = create(dataJoinColumns[i]);
		}
		return mDataJoinColumns;
	}
}
