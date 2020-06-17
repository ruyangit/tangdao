package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataSqlSegment;
import com.tangdao.core.mybatis.data.privilege.model.MDataSqlSegment;

/**
 * 
 * @ClassName: MDataSqlSegmentUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataSqlSegmentUtil {

	/**
	 * create MDataSqlSegment by @DataSqlSegment.
	 * 
	 * @param dataSqlSegment
	 * @return
	 * @return MDataSqlSegment
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataSqlSegment create(DataSqlSegment dataSqlSegment) {
		MDataSqlSegment mDataSqlSegment = new MDataSqlSegment();
		mDataSqlSegment.setSql(dataSqlSegment.sql());
		mDataSqlSegment.setValues(dataSqlSegment.values());
		mDataSqlSegment.setCategoryKeys(dataSqlSegment.categoryKeys());
		mDataSqlSegment.setVar(dataSqlSegment.val());
		return mDataSqlSegment;
	}
	
	/**
	 * create MDataSqlSegment by @DataSqlSegment.
	 * 
	 * @param dataSqlSegments
	 * @return
	 * @return MDataSqlSegment[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataSqlSegment[] create(DataSqlSegment[] dataSqlSegments) {
		MDataSqlSegment[] mDataSqlSegments = new MDataSqlSegment[dataSqlSegments.length];
		for (int i = 0; i < dataSqlSegments.length; i++) {
			mDataSqlSegments[i] = create(dataSqlSegments[i]);
		}
		return mDataSqlSegments;
	}
}
