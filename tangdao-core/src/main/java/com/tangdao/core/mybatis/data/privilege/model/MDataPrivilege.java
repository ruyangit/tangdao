package com.tangdao.core.mybatis.data.privilege.model;

/**
 * 
 * @ClassName: MDataPrivilege.java
 * @author: Naughty Guo
 * @date: Jun 3, 2016
 */
public class MDataPrivilege {

	private MDataJoinRelation[] relations;
	private MDataSqlSegment[] segments;
	private MDataCondition[] conditions;

	public MDataJoinRelation[] getRelations() {
		return relations;
	}

	public void setRelations(MDataJoinRelation[] relations) {
		this.relations = relations;
	}

	public MDataSqlSegment[] getSegments() {
		return segments;
	}

	public void setSegments(MDataSqlSegment[] segments) {
		this.segments = segments;
	}

	public MDataCondition[] getConditions() {
		return conditions;
	}

	public void setConditions(MDataCondition[] conditions) {
		this.conditions = conditions;
	}
}
