package com.tangdao.core.mybatis.data.privilege.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;
import com.tangdao.core.mybatis.data.privilege.factory.ExpressionFactory;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataColumn;
import com.tangdao.core.mybatis.data.privilege.model.MDataCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinRelation;
import com.tangdao.core.mybatis.data.privilege.model.MDataObject;
import com.tangdao.core.mybatis.data.privilege.model.MDataPrivilege;
import com.tangdao.core.mybatis.data.privilege.model.MDataSqlSegment;
import com.tangdao.core.mybatis.data.privilege.model.SqlMetadata;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * CommonPrivilegeUtil
 * 
 * @ClassName: 
 * @author: Naughty Guo
 * @date: 2016年7月16日
 */
public class CommonPrivilegeUtil {

	/**
	 * get alias name.
	 * 
	 * @param table
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月16日
	 */
	public static String getAliasName(Table table) {
		if (null == table.getAlias()) {
			return "";
		}
		if (StringUtils.isBlank(table.getAlias().getName())) {
			return "";
		}
		return table.getAlias().getName().toLowerCase().trim();
	}
	
	/**
	 * get table name.
	 * 
	 * @param table
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月16日
	 */
	public static String getTableName(Table table) {
		if (StringUtils.isBlank(table.getName())) {
			return "";
		}
		return table.getName().toLowerCase().trim();
	}
	
	/**
	 * get table full name.
	 * 
	 * @param table
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月17日
	 */
	public static String getTableFullName(Table table) {
		return getTableName(table) + ":" + getAliasName(table);
	}

	/**
	 * handle SQL segment.
	 * 
	 * @param dataPrivilegeParameter
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月13日
	 */
	public static String handleSqlSegments(DataPrivilegeParameter dataPrivilegeParameter) {
		boolean hasHandleCategoryKeys = false;
		String querySql = dataPrivilegeParameter.getOriginalSql(), replaceSql = null;
		MDataSqlSegment[] segments = dataPrivilegeParameter.getMDataPrivilege().getSegments();
		if (null != segments && segments.length > 0) {
			Map<String, Object> privilegeData = dataPrivilegeParameter.getPrivilegeData();
			for (MDataSqlSegment mDataSqlSegment : segments) {
				replaceSql = mDataSqlSegment.getSql();
				String[] values = mDataSqlSegment.getValues();
				String[] categoryKeys = mDataSqlSegment.getCategoryKeys();
				for (int i = 0; i < categoryKeys.length; i++) {
					hasHandleCategoryKeys = true;
					replaceSql = replaceSql.replace(":" + i, "'" + String.valueOf(privilegeData.get(categoryKeys[i])) + "'");
				}
				if (!hasHandleCategoryKeys) {
					for (int i = 0; i < values.length; i++) {
						replaceSql = replaceSql.replace(":" + i, "'" + String.valueOf(values[i]) + "'");
					}
				}
				querySql = querySql.replaceAll("[{]" + mDataSqlSegment.getVar() + "[}]", replaceSql);
			}
		}
		return querySql;
	}

	/**
	 * remove illegal joins.
	 * 
	 * @param selectBody
	 * @param dataPrivilegeParameter
	 * @return
	 * void
	 * @author Naughty Guo 2016年7月17日
	 */
	public static void removeIllegalJoins(SelectBody selectBody, DataPrivilegeParameter dataPrivilegeParameter) {
		SqlMetadata sqlMetadata = null;
		Map<String, List<String>> allJoins = new HashMap<String, List<String>>();
		Set<String> allLegalJoins = new HashSet<String>();
		MDataJoinRelation[] relations = dataPrivilegeParameter.getMDataPrivilege().getRelations();
		if (null != relations && relations.length > 0) {
			sqlMetadata = getLegalObjectInfo(selectBody);
			allJoins.putAll(sqlMetadata.getLegalJoins());
			fillAnnotationJoins(relations, allJoins);
			dataPrivilegeParameter.setLegalObjects(sqlMetadata.getLegalObjects());
			dataPrivilegeParameter.setAllJoins(allJoins);
			for (String legalObject : sqlMetadata.getLegalObjects()) {
				if (allJoins.containsKey(legalObject)) {
					allLegalJoins.add(legalObject);
					allLegalJoins.addAll(allJoins.get(legalObject));
				}
			}
			sqlMetadata.setAllLegalJoins(allLegalJoins);
			removeIllegalJoins(dataPrivilegeParameter, allLegalJoins);
		}
	}
	
	/**
	 * get legal SQL metadata info.
	 * 
	 * @param selectBody
	 * @return
	 * SqlMetadata
	 * @author Naughty Guo 2016年7月17日
	 */
	public static SqlMetadata getLegalObjectInfo(SelectBody selectBody) {
		SqlMetadata sqlMetadata = new SqlMetadata();
		Set<String> legalObjects = new HashSet<String>();
		Map<String, List<String>> legalJoins = new HashMap<String, List<String>>();
		sqlMetadata.setLegalObjects(legalObjects);
		sqlMetadata.setLegalJoins(legalJoins);
		if (selectBody instanceof PlainSelect) {
			getLegalObjectInfo(selectBody, legalObjects, legalJoins);
		}
		return sqlMetadata;
	}
	
	/**
	 * assembly where expression.
	 *
	 * @param dataPrivilegeParameter
	 * @param table
	 * @param whereExpression
	 * @return
	 * @return Expression
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static Expression assemblyWhere(DataPrivilegeParameter dataPrivilegeParameter, Table table, Expression whereExpression) {
		Expression middleExpression = null;		
		MDataCondition[] conditions = dataPrivilegeParameter.getMDataPrivilege().getConditions();
		if (conditions.length > 0) {
			for (MDataCondition mDataCondition : conditions) {
				MDataObject mDataObject = mDataCondition.getReference();
				if (CommonPrivilegeUtil.getTableFullName(table).equals(mDataObject.getFullName())) {
					String categoryGroup = mDataCondition.getCategoryGroup();
					if (StringUtils.isNotBlank(categoryGroup)) {
						middleExpression = ExpressionFactory.createCombineExpression(mDataCondition.getCombineType(), middleExpression, 
								ExpressionFactory.generateGroupExpression(mDataCondition, dataPrivilegeParameter));
					} else {
						middleExpression = ExpressionFactory.createCombineExpression(mDataCondition.getCombineType(), middleExpression, 
								ExpressionFactory.createSingleExpressioin(mDataCondition, dataPrivilegeParameter));
					}
				}
			}
		}
		if (null != whereExpression && null != middleExpression) {
			middleExpression = ExpressionFactory.createParenthesis(middleExpression);
			whereExpression = ExpressionFactory.createCombineExpression(CombineType.AND, whereExpression, middleExpression);
		} 
		if (null == whereExpression) {
			whereExpression = middleExpression;
		}
		return whereExpression;
	}
	
	/**
	 * check whether use join object with join condition.
	 * 
	 * @param dataPrivilegeParameter
	 * @param mDataJoinRelation
	 * @return boolean
	 * @author Naughty Guo 2016年7月18日
	 */
	public static boolean isUseJoinWithJoinConditionData(DataPrivilegeParameter dataPrivilegeParameter, MDataJoinRelation mDataJoinRelation) {
		MDataColumn[] mDataColumns = null;
		MDataJoinCondition[] mDataJoinConditions = mDataJoinRelation.getConditions();
		for (MDataJoinCondition mDataJoinCondition : mDataJoinConditions) {
			mDataColumns = mDataJoinCondition.getColumns();
			for (MDataColumn mDataColumn : mDataColumns) {
				if (dataPrivilegeParameter.doConditionable(mDataColumn)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * check whether use join object with where condition.
	 * 
	 * @param dataPrivilegeParameter
	 * @param mDataJoinRelation
	 * @return boolean
	 * @author Naughty Guo 2016年7月18日
	 */
	public static boolean isUseJoinWithWhereData(DataPrivilegeParameter dataPrivilegeParameter, MDataJoinRelation mDataJoinRelation) {
		MDataColumn[] mDataColumns = null;
		MDataCondition[] mDataConditions = dataPrivilegeParameter.getMDataPrivilege().getConditions();
		for (MDataCondition mDataCondition : mDataConditions) {
			mDataColumns = mDataCondition.getColumns();
			for (MDataColumn mDataColumn : mDataColumns) {
				if (dataPrivilegeParameter.doConditionable(mDataColumn)) {
					return checkWhetherJoinAvailable(dataPrivilegeParameter, mDataJoinRelation, mDataColumn.getObject().getFullName());
				}
			}
		}
		return false;
	}
	
	/**
	 * check whether contains target join.
	 *
	 * @param joins
	 * @param mDataJoinRelation
	 * @return
	 * @return boolean
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static boolean containsJoin(List<Join> joins, MDataJoinRelation mDataJoinRelation) {
		Table table = null;
		if (null == joins || joins.isEmpty()) {
			return false;
		}
		for (Join join : joins) {
			if (join.getRightItem() instanceof Table) {
				table = (Table) join.getRightItem();
				if (getTableFullName(table).equals(mDataJoinRelation.getSlave().getFullName())) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * check whether generate join relation.
	 *
	 * @param dataPrivilegeParameter
	 * @param mDataJoinRelation
	 * @param objectName
	 * @return
	 * @return boolean
	 * @author Naughty Guo 
	 * @date Mar 19, 2018
	 */
	private static boolean checkWhetherJoinAvailable(DataPrivilegeParameter dataPrivilegeParameter, MDataJoinRelation mDataJoinRelation, String objectName) {
		Map<String, List<String>> allJoins = dataPrivilegeParameter.getAllJoins();
		for (Map.Entry<String, List<String>> join : allJoins.entrySet()) {
			if (join.getValue().contains(objectName) && join.getKey().equals(mDataJoinRelation.getMaster().getFullName())) {
				if (join.getValue().contains(mDataJoinRelation.getSlave().getFullName()) && 
						((null != allJoins.get(mDataJoinRelation.getSlave().getFullName()) && 
							allJoins.get(mDataJoinRelation.getSlave().getFullName()).contains(objectName)) || 
								join.getValue().contains(objectName))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * get legal objects.
	 * 
	 * @param selectBody
	 * @param legalObjects
	 * void
	 * @author Naughty Guo 2016年7月16日
	 */
	private static void getLegalObjectInfo(SelectBody selectBody, Set<String> legalObjects, Map<String, List<String>> legalJoins) {
		Table table = null;
		FromItem fromItem = null;
		PlainSelect plainSelect = null;
		if (selectBody instanceof PlainSelect) {
			plainSelect = ((PlainSelect) selectBody);
			fromItem = plainSelect.getFromItem();
			if (fromItem instanceof Table) {
				table = (Table) fromItem;
				legalObjects.add(getTableFullName(table));
			} else if (fromItem instanceof SubSelect) {
				SubSelect subSelect = (SubSelect) fromItem;
				if (subSelect.getSelectBody() instanceof PlainSelect) {
					getLegalObjectInfo(subSelect.getSelectBody(), legalObjects, legalJoins);
				}
			}
			getLegalObjectJoinInfo(plainSelect, table, legalObjects, legalJoins);
		}
	}
	
	/**
	 * get legal object join info.
	 * 
	 * @param plainSelect
	 * @param table
	 * @param legalObjects
	 * @param legalJoins
	 * void
	 * @author Naughty Guo 2016年7月18日
	 */
	private static void getLegalObjectJoinInfo(PlainSelect plainSelect, Table table, Set<String> legalObjects, Map<String, List<String>> legalJoins) {
		FromItem fromItem = null;
		List<Join> joins = null;
		List<String> tempJoins = null;
		joins = plainSelect.getJoins();
		if (null != joins) {
			for (Join join : joins) {
				if (null != table) {
					if (legalJoins.containsKey(getTableFullName(table))) {
						tempJoins = legalJoins.get(getTableFullName(table));
					} else {
						tempJoins = new ArrayList<String>();
						legalJoins.put(getTableFullName(table), tempJoins);
					}
				}
				fromItem = join.getRightItem();
				if (fromItem instanceof Table) {
					table = (Table) fromItem;
					tempJoins.add(getTableFullName(table));
					legalObjects.add(getTableFullName(table));
				} else if (fromItem instanceof SubSelect) {
					SubSelect subSelect = (SubSelect) fromItem;
					if (subSelect.getSelectBody() instanceof PlainSelect) {
						getLegalObjectInfo(subSelect.getSelectBody(), legalObjects, legalJoins);
					}
				}
			}
		}
	}
	
	/**
	 * fill all annotation joins.
	 * 
	 * @param relations
	 * @param allJoins
	 * @return
	 * Map<String,Set<String>>
	 * @author Naughty Guo 2016年7月17日
	 */
	private static void fillAnnotationJoins(MDataJoinRelation[] relations, Map<String, List<String>> allJoins) {
		List<String> slaves = null;
		boolean existsAnnotionJoin = false;
		String masterkey = null, slaveKey = null;
		for (MDataJoinRelation mDataJoinRelation : relations) {
			masterkey = mDataJoinRelation.getMaster().getFullName();
			slaveKey = mDataJoinRelation.getSlave().getFullName();
			if (!allJoins.containsKey(masterkey)) {
				slaves = new ArrayList<String>();
				slaves.add(slaveKey);
				allJoins.put(masterkey, slaves);
			} else {
				for (String annotationJoin : allJoins.get(masterkey)) {
					if (annotationJoin.equals(slaveKey)) {
						existsAnnotionJoin = true;
						break;
					}
				}
				if (!existsAnnotionJoin) {
					allJoins.get(masterkey).add(slaveKey);
				}
			}
		}
		fillAnnotationJoins(allJoins);
	}
	
	/**
	 * fill annotation join to target object list.
	 * 
	 * @param allJoins
	 * void
	 * @author Naughty Guo 2016年7月17日
	 */
	private static void fillAnnotationJoins(Map<String, List<String>> allJoins) {
		List<String> middleDynamicJoins = null;
		for (Map.Entry<String, List<String>> entry : allJoins.entrySet()) {
			middleDynamicJoins = new ArrayList<String>();
			for (String itemKey : entry.getValue()) {
				if (null != allJoins.get(itemKey)) {
					fillAnnotationJoins(allJoins.get(itemKey), allJoins, middleDynamicJoins);
				}
			}
			entry.getValue().addAll(middleDynamicJoins);
		}
	}
	
	/**
	 * fill annotation join to target object list.
	 * 
	 * @param children
	 * @param allJoins
	 * @param result
	 * void
	 * @author Naughty Guo 2016年7月17日
	 */
	private static void fillAnnotationJoins(List<String> children, Map<String, List<String>> allJoins, List<String> result) {
		if (null != children && children.size() > 0) {
			for (String item : children) {
				result.add(item);
				if (null != allJoins.get(item)) {
					fillAnnotationJoins(allJoins.get(item), allJoins, result);
				}
			}
		}
	}
	
	/**
	 * remove illegal joins.
	 * 
	 * @param dataPrivilegeParameter
	 * @param allLegalJoins
	 * void
	 * @author Naughty Guo 2016年7月17日
	 */
	private static void removeIllegalJoins(DataPrivilegeParameter dataPrivilegeParameter, Set<String> allLegalJoins) {
		MDataJoinRelation[] relations = null;
		List<MDataJoinRelation> mDataJoinRelations = null;
		MDataPrivilege mDataPrivilege = dataPrivilegeParameter.getMDataPrivilege();
		if (null == allLegalJoins || allLegalJoins.size() == 0) {
			mDataPrivilege.setRelations(new MDataJoinRelation[0]);
		}
		relations = mDataPrivilege.getRelations();
		mDataJoinRelations = new ArrayList<MDataJoinRelation>();
		for (MDataJoinRelation mDataJoinRelation : relations) {
			if (allLegalJoins.contains(mDataJoinRelation.getMaster().getFullName())) {
				mDataJoinRelations.add(mDataJoinRelation);
			}
		}
		removeNoDataJoins(dataPrivilegeParameter, mDataJoinRelations);
		mDataJoinRelations = getSortDataJoinRelations(mDataJoinRelations, dataPrivilegeParameter.getLegalObjects());
		mDataPrivilege.setRelations(mDataJoinRelations.toArray(new MDataJoinRelation[mDataJoinRelations.size()]));
	}
	
	/**
	 * remove no data joins.
	 * 
	 * @param dataPrivilegeParameter
	 * @param mDataJoinRelations
	 * void
	 * @author Naughty Guo 2016年7月18日
	 */
	private static void removeNoDataJoins(DataPrivilegeParameter dataPrivilegeParameter, List<MDataJoinRelation> mDataJoinRelations) {
		MDataJoinRelation mDataJoinRelation = null;
		for (int i = 0; i < mDataJoinRelations.size(); i++) {
			mDataJoinRelation = mDataJoinRelations.get(i);
			if (!isUseJoinWithJoinConditionData(dataPrivilegeParameter, mDataJoinRelation) && 
					!isUseJoinWithWhereData(dataPrivilegeParameter, mDataJoinRelation)) {
				mDataJoinRelations.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * get sort data join relations.
	 * 
	 * @param mDataJoinRelations
	 * @param legalJoins
	 * @return
	 * List<MDataJoinRelation>
	 * @author Naughty Guo 2016年7月17日
	 */
	private static List<MDataJoinRelation> getSortDataJoinRelations(List<MDataJoinRelation> mDataJoinRelations, Set<String> legalObjects) {
		List<MDataJoinRelation> sortDataJoinRelations = new ArrayList<MDataJoinRelation>();
		while (mDataJoinRelations.size() != 0) {
			for (int i = 0; i < mDataJoinRelations.size(); i++) {
				MDataJoinRelation mDataJoinRelation = mDataJoinRelations.get(i);
				if (legalObjects.contains(mDataJoinRelation.getMaster().getFullName())) {
					sortDataJoinRelations.add(mDataJoinRelation);
					mDataJoinRelations.remove(i);
					break;
				} else {
					for (int j = 0; j < sortDataJoinRelations.size(); j++) {
						MDataJoinRelation sortDataJoinRelation = sortDataJoinRelations.get(j);
						if (sortDataJoinRelation.getSlave().getFullName().equals(mDataJoinRelation.getMaster().getFullName())) {
							sortDataJoinRelations.add(j + 1, mDataJoinRelation);
							mDataJoinRelations.remove(i);
							break;
						}
					}
				}
			}
		}
		return sortDataJoinRelations;
	}
}
