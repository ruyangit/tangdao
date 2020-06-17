package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataJoinRelation;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinRelation;

/**
 * 
 * @ClassName: MDataJoinRelationUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataJoinRelationUtil {

	/**
	 * create MDataJoinRelation by @DataJoinRelation.
	 * 
	 * @param dataJoinRelation
	 * @return
	 * @return MDataJoinRelation
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataJoinRelation create(DataJoinRelation dataJoinRelation) {
		MDataJoinRelation mDataJoinRelation = new MDataJoinRelation();
		mDataJoinRelation.setConditions(MDataJoinConditionUtil.create(dataJoinRelation.conditions()));
		mDataJoinRelation.setJoinColumns(MDataJoinColumnUtil.create(dataJoinRelation.joinColumns()));
		mDataJoinRelation.setJoinType(dataJoinRelation.joinType());
		mDataJoinRelation.setMaster(MDataObjectUtil.create(dataJoinRelation.master()));
		mDataJoinRelation.setSlave(MDataObjectUtil.create(dataJoinRelation.slave()));
		return mDataJoinRelation;
	}
	
	/**
	 * create MDataJoinRelation by @DataJoinRelation.
	 * 
	 * @param dataJoinRelations
	 * @return
	 * @return MDataJoinRelation[]
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataJoinRelation[] create(DataJoinRelation[] dataJoinRelations) {
		MDataJoinRelation[] mDataJoinRelations = new MDataJoinRelation[dataJoinRelations.length];
		for (int i = 0; i < dataJoinRelations.length; i++) {
			mDataJoinRelations[i] = create(dataJoinRelations[i]);
		}
		return mDataJoinRelations;
	}
}
