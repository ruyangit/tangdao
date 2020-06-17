package com.tangdao.core.mybatis.data.privilege.factory;

import java.util.ArrayList;
import java.util.List;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ObjectJoinType;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinColumn;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinRelation;
import com.tangdao.core.mybatis.data.privilege.model.MDataObject;
import com.tangdao.core.mybatis.data.privilege.model.MDataPrivilege;
import com.tangdao.core.mybatis.data.privilege.model.SqlMetadata;
import com.tangdao.core.mybatis.data.privilege.util.CommonPrivilegeUtil;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;

/**
 * JoinFactory
 * 
 * @ClassName: 
 * @author: Naughty Guo
 * @date: 2016年7月16日
 */
public class JoinFactory {

	/**
	 * create joins.
	 * 
	 * @param sqlMetadata
	 * @param dataPrivilegeParameter
	 * @return
	 * List<Join>
	 * @author Naughty Guo 2016年7月16日
	 */
	public static List<Join> createJions(SqlMetadata sqlMetadata, DataPrivilegeParameter dataPrivilegeParameter) {
		Join join = null;
		List<String> allJoins = null;
		List<Join> joins = sqlMetadata.getJoins();
		String masterKey = null, slaveKey = null;
		MDataJoinRelation[] relations = dataPrivilegeParameter.getMDataPrivilege().getRelations();
		if (null != relations && relations.length > 0) {
			if (null == joins) {				
				joins = new ArrayList<Join>();
			}
			allJoins = dataPrivilegeParameter.getAllJoins().get(CommonPrivilegeUtil.getTableFullName(sqlMetadata.getTable()));
			allJoins.add(CommonPrivilegeUtil.getTableFullName(sqlMetadata.getTable()));
			for (MDataJoinRelation mDataJoinRelation : relations) {
				masterKey = mDataJoinRelation.getMaster().getFullName();
				slaveKey = mDataJoinRelation.getSlave().getFullName();
				if (allJoins.contains(masterKey) && allJoins.contains(slaveKey)) {
					join = getMatchedJoin(sqlMetadata, mDataJoinRelation);
					if (null != join) {
						updateJoinCodition(join, mDataJoinRelation, dataPrivilegeParameter);
					} else {
						join = createJoinWithCondition(mDataJoinRelation, dataPrivilegeParameter);
					}
				}
				if (null != join && !joins.contains(join)) {
					joins.add(join);
				}
			}
		}
		return joins;
	}
	
	/**
	 * create joins.
	 *
	 * @param dataPrivilegeParameter
	 * @return
	 * @return List<Join>
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static List<Join> createSimpleJoins(DataPrivilegeParameter dataPrivilegeParameter) {
		Join join = null;
		List<Join> joins = new ArrayList<Join>();
		MDataPrivilege dataPrivilege = dataPrivilegeParameter.getMDataPrivilege();
		MDataJoinRelation[] relations = dataPrivilege.getRelations();
		if (null != relations && relations.length > 0) {
			for (MDataJoinRelation mDataJoinRelation : relations) {
				if (CommonPrivilegeUtil.isUseJoinWithJoinConditionData(dataPrivilegeParameter, mDataJoinRelation) || 
						CommonPrivilegeUtil.isUseJoinWithWhereData(dataPrivilegeParameter, mDataJoinRelation)) {
					if (!CommonPrivilegeUtil.containsJoin(joins, mDataJoinRelation)) {
						join = JoinFactory.createSimple(mDataJoinRelation);
					}
					join = JoinFactory.createJoinWithCondition(mDataJoinRelation, dataPrivilegeParameter);
					joins.add(join);
				}
			}
		}
		return joins;
	}
	
	/**
	 * create simple join.
	 *
	 * @param mDataJoinRelation
	 * @return
	 * @return Join
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static Join createSimple(MDataJoinRelation mDataJoinRelation) {
		Join join = new Join();
		MDataObject dataObject = mDataJoinRelation.getSlave();
		join.setRightItem(TableFactory.createTable(dataObject));
		return join;
	}
	
	/**
	 * create join with conditions.
	 * 
	 * @param mDataJoinRelation
	 * @param dataPrivilegeParameter
	 * @return
	 * Join
	 * @author Naughty Guo 2016年7月16日
	 */
	public static Join createJoinWithCondition(MDataJoinRelation mDataJoinRelation, DataPrivilegeParameter dataPrivilegeParameter) {
		Join join = null;
		MDataObject slaveDataObject = mDataJoinRelation.getSlave();
		Expression expression = null;
		Expression joinExpression = setJoinColumns(mDataJoinRelation);
		Expression joinWhereExpression = setJoinWhereConditions(mDataJoinRelation, dataPrivilegeParameter);
		if (null != joinExpression && null != joinWhereExpression) {
			expression = ExpressionFactory.createCombineExpression(CombineType.AND, joinExpression, joinWhereExpression);
		}
		if (null != joinExpression) {
			expression = joinExpression;
		}
		if (null != joinWhereExpression) {
			expression = joinWhereExpression;
		}
		if (null == expression) {
			return null;
		}
		join = new Join();
		join.setOnExpression(ExpressionFactory.createParenthesis(expression));
		join.setRightItem(TableFactory.createTable(slaveDataObject.getName(), slaveDataObject.getAlias()));
		setJoinType(mDataJoinRelation, join);
		return join;
	}
	
	/**
	 * add join condition.
	 * 
	 * @param join
	 * @param mDataJoinRelation
	 * @param dataPrivilegeParameter
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	public static void updateJoinCodition(Join join, MDataJoinRelation mDataJoinRelation, DataPrivilegeParameter dataPrivilegeParameter) {
		Expression middleExpression = null;
		Expression expression = join.getOnExpression();
		Expression joinExpression = setJoinColumns(mDataJoinRelation);
		Expression joinWhereExpression = setJoinWhereConditions(mDataJoinRelation, dataPrivilegeParameter);
		if (null != joinExpression && null != joinWhereExpression) {
			middleExpression = ExpressionFactory.createCombineExpression(CombineType.AND, joinExpression, joinWhereExpression);
		}
		if (null != joinExpression) {
			middleExpression = joinExpression;
		}
		if (null != joinWhereExpression) {
			middleExpression = joinWhereExpression;
		}
		if (null == middleExpression) {
			return;
		}
		ExpressionFactory.createParenthesis(middleExpression);
		expression = ExpressionFactory.createCombineExpression(CombineType.AND, expression, middleExpression);
		join.setOnExpression(expression);
	}
	
	/**
	 * get join type.
	 * 
	 * @param join
	 * @return
	 * ObjectJoinType
	 * @author Naughty Guo 2016年7月16日
	 */
	private static ObjectJoinType getObjectJoinType(Join join) {
		if (join.isFull()) {
			return ObjectJoinType.FULL;
		} else if (join.isInner()) {
			return ObjectJoinType.INNER;
		} else if (join.isLeft()) {
			return ObjectJoinType.LEFT;
		} else if (join.isRight()) {
			return ObjectJoinType.RIGHT;
		} else if (join.isOuter()) {
			return ObjectJoinType.OUTER;
		}
		return null;
	}
	
	/**
	 * get match join.
	 * 
	 * @param sqlMetadata
	 * @param mDataJoinRelation
	 * @return
	 * Join
	 * @author Naughty Guo 2016年7月16日
	 */
	public static Join getMatchedJoin(SqlMetadata sqlMetadata, MDataJoinRelation mDataJoinRelation) {
		List<Join> joins = sqlMetadata.getJoins();
		Table originalTable = sqlMetadata.getTable(), joinTalbe = null;
		MDataObject masterDataObject = mDataJoinRelation.getMaster();
		MDataObject slaveDataObject = mDataJoinRelation.getSlave();
		if (masterDataObject.getFullName().equals(CommonPrivilegeUtil.getTableFullName(originalTable))) {
			if (null == joins) {
				return null;
			} else {
				for (Join join : joins) {
					if (join.getRightItem() instanceof Table) {
						joinTalbe = (Table) join.getRightItem();
						if (slaveDataObject.getFullName().equals(CommonPrivilegeUtil.getTableFullName(joinTalbe)) && 
								mDataJoinRelation.getJoinType() == getObjectJoinType(join)) {
							return join;
						}
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * set join column conditions.
	 * 
	 * @param mDataJoinRelation
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月16日
	 */
	private static Expression setJoinWhereConditions(MDataJoinRelation mDataJoinRelation, DataPrivilegeParameter dataPrivilegeParameter) {
		Expression expression = null, middleExpression = null;
		MDataJoinCondition[] conditions = mDataJoinRelation.getConditions();
		if (null != conditions && conditions.length > 0) {
			for (MDataJoinCondition mDataJoinCondition : conditions) {
				middleExpression = ExpressionFactory.createSingleJoinExpressioin(mDataJoinCondition, dataPrivilegeParameter);
				if (null == expression) {
					expression = ExpressionFactory.createParenthesis(middleExpression);
				} else {
					middleExpression = ExpressionFactory.createParenthesis(middleExpression);;
					expression = ExpressionFactory.createParenthesis(ExpressionFactory.createCombineExpression(mDataJoinCondition.getCombineType(), expression, middleExpression));
				}
			}
		}
		return expression;
	}
	
	/**
	 * set join columns.
	 * 
	 * @param mDataJoinRelation
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月16日
	 */
	private static Expression setJoinColumns(MDataJoinRelation mDataJoinRelation) {
		BinaryExpression binaryExpression = null;
		Expression expression = null, leftExpression = null, rightExpression = null;
		MDataObject masterDataObject = mDataJoinRelation.getMaster();
		MDataObject slaveDataObject = mDataJoinRelation.getSlave();
		MDataJoinColumn[] joinColumns = mDataJoinRelation.getJoinColumns();
		if (null != joinColumns && joinColumns.length > 0) {
			for (MDataJoinColumn mDataJoinColumn : joinColumns) {
				leftExpression = ExpressionFactory.createColumnExpression(masterDataObject.getAlias(), mDataJoinColumn.getMasterColumn());
				rightExpression = ExpressionFactory.createColumnExpression(slaveDataObject.getAlias(), mDataJoinColumn.getSlaveColumn());
				binaryExpression = ExpressionFactory.createBinaryExpression(mDataJoinColumn.getOperationType());
				binaryExpression.setLeftExpression(leftExpression);
				binaryExpression.setRightExpression(rightExpression);
				if (null == expression) {
					expression = ExpressionFactory.createParenthesis(binaryExpression);
				} else {
					expression = ExpressionFactory.createParenthesis(ExpressionFactory.createCombineExpression(CombineType.AND, expression, 
							ExpressionFactory.createParenthesis(binaryExpression)));
				}
			}
		}
		return expression;
	}
	
	/**
	 * set join type.
	 * 
	 * @param mDataJoinRelation
	 * @param join
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	private static void setJoinType(MDataJoinRelation mDataJoinRelation, Join join) {
		if (mDataJoinRelation.getJoinType() == ObjectJoinType.FULL) {
			join.setFull(true);
		} else if (mDataJoinRelation.getJoinType() == ObjectJoinType.INNER) {
			join.setInner(true);
		} else if (mDataJoinRelation.getJoinType() == ObjectJoinType.LEFT) {
			join.setLeft(true);
		} else if (mDataJoinRelation.getJoinType() == ObjectJoinType.RIGHT) {
			join.setRight(true);
		} else {
			join.setOuter(true);
		}
	}
}
