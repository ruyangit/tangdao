package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataJoinCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinCondition;

/**
 * 
 * @ClassName: MDataJoinConditionUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinConditionUtil {

	/**
	 * create MDataJoinCondition by @DataJoinCondition.
	 * 
	 * @param dataJoinCondition
	 * @return
	 * @return MDataJoinCondition
	 * @author Naughty Guo Jun 3, 2016
	 */
	/**
	 * @param dataJoinCondition
	 * @return
	 * @return MDataJoinCondition
	 * @author Naughty Guo Jun 11, 2016
	 */
	public static MDataJoinCondition create(DataJoinCondition dataJoinCondition) {
		MDataJoinCondition mDataJoinCondition = new MDataJoinCondition();
		mDataJoinCondition.setCombineType(dataJoinCondition.combineType());
		mDataJoinCondition.setColumns(MDataColumnUtil.create(dataJoinCondition.columns()));
		return mDataJoinCondition;
	}
	
	/**
	 * create MDataJoinCondition by @DataJoinCondition.
	 * 
	 * @param dataJoinConditions
	 * @return
	 * @return MDataJoinCondition[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataJoinCondition[] create(DataJoinCondition[] dataJoinConditions) {
		MDataJoinCondition[] mDataJoinConditions = new MDataJoinCondition[dataJoinConditions.length];
		for (int i = 0; i < dataJoinConditions.length; i++) {
			mDataJoinConditions[i] = create(dataJoinConditions[i]);
		}
		return mDataJoinConditions;
	}
}
