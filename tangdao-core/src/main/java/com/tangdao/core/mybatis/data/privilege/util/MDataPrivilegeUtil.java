package com.tangdao.core.mybatis.data.privilege.util;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.model.MDataPrivilege;

/**
 * 
 * @ClassName: MDataPrivilegeUtil.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataPrivilegeUtil {

	/**
	 * create MDataPrivilege by @DataPrivilege.
	 * 
	 * @param dataPrivilege
	 * @return
	 * @return MDataPrivilege
	 * @author Naughty Guo Jun 3, 2016
	 */
	public static MDataPrivilege create(DataPrivilege dataPrivilege) {
		MDataPrivilege mDataPrivilege = new MDataPrivilege();
		mDataPrivilege.setConditions(MDataConditionUtil.create(dataPrivilege.conditions()));
		mDataPrivilege.setRelations(MDataJoinRelationUtil.create(dataPrivilege.relations()));
		mDataPrivilege.setSegments(MDataSqlSegmentUtil.create(dataPrivilege.segments()));
		return mDataPrivilege;
	}
}
