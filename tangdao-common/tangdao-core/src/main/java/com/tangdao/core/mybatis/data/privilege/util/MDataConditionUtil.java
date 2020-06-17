package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataCondition;

/**
 * 
 * @ClassName: MDataConditionUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataConditionUtil {

	/**
	 * create MDataCondition by @DataCondition.
	 * 
	 * @param dataCondition
	 * @return
	 * @return MDataCondition
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataCondition create(DataCondition dataCondition) {
		MDataCondition mDataCondition = new MDataCondition();
		mDataCondition.setCombineType(dataCondition.combineType());
		mDataCondition.setColumns(MDataColumnUtil.create(dataCondition.columns()));
		mDataCondition.setCategoryGroup(dataCondition.categoryGroup());
		mDataCondition.setGroupCombineType(dataCondition.groupCombineType());
		mDataCondition.setReference(MDataObjectUtil.create(dataCondition.reference()));
		return mDataCondition;
	}
	
	/**
	 * create MDataCondition by @DataCondition.
	 * 
	 * @param dataConditions
	 * @return
	 * @return MDataCondition[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataCondition[] create(DataCondition[] dataConditions) {
		MDataCondition[] mDataConditions = new MDataCondition[dataConditions.length];
		for (int i = 0; i < dataConditions.length; i++) {
			mDataConditions[i] = create(dataConditions[i]);
		}
		return mDataConditions;
	}
}
