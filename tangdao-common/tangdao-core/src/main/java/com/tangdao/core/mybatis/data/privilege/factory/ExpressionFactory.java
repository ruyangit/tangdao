package com.tangdao.core.mybatis.data.privilege.factory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.ColumnType;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.CombineType;
import com.tangdao.core.mybatis.data.privilege.constant.DataPrivilegeConstant.OperationType;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataColumn;
import com.tangdao.core.mybatis.data.privilege.model.MDataCondition;
import com.tangdao.core.mybatis.data.privilege.model.MDataJoinCondition;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;

/**
 * 
 * @ClassName: ExpressionFactory.java
 * @author: Naughty Guo
 * @date: Jun 10, 2016
 */
public class ExpressionFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExpressionFactory.class);
	
	/**
	 * create binary expression instance.
	 * 
	 * @param operationType
	 * @return
	 * @return Expression
	 * @author Naughty Guo Jun 10, 2016
	 */
	public static BinaryExpression createBinaryExpression(MDataColumn mDataColumn) {
		return createBinaryExpression(mDataColumn.getOperationType());
	}
	
	/**
	 * create binary expression instance.
	 * 
	 * @param operationType
	 * @return
	 * @return Expression
	 * @author Naughty Guo Jun 10, 2016
	 */
	public static BinaryExpression createBinaryExpression(OperationType operationType) {
		BinaryExpression expression = null;
		if (operationType == OperationType.EQUAL) {
			expression = new EqualsTo();
		} else if (operationType == OperationType.NOT_EQUAL) {
			expression = new NotEqualsTo();
		} else if (operationType == OperationType.GREATER_THAN) {
			expression = new GreaterThan();
		} else if (operationType == OperationType.GREATER_THAN_EQUAL) {
			expression = new GreaterThanEquals();
		} else if (operationType == OperationType.MINOR_THAN) {
			expression = new MinorThan();
		} else if (operationType == OperationType.MINOR_THAN_EQUAL) {
			expression = new MinorThanEquals();
		} else if (operationType == OperationType.FULL_LIKE) {
			expression = new LikeExpression();
		} else if (operationType == OperationType.LEFT_LIKE) {
			expression = new LikeExpression();
		} else if (operationType == OperationType.RIGHT_LIKE) {
			expression = new LikeExpression();
		}
		return expression;
	}
	
	/**
	 * create StringValue expression instance.
	 * 
	 * @param mDataColumn
	 * @param dataPrivilegeParameter
	 * @return
	 * StringValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createStringValueExpression(MDataColumn mDataColumn, DataPrivilegeParameter dataPrivilegeParameter) {
		return createStringValueExpression(mDataColumn, dataPrivilegeParameter.getPrivilegeData(mDataColumn));
	}
	
	/**
	 * create StringValue expression instance.
	 * 
	 * @param mDataColumn
	 * @param targetValue
	 * @return
	 * StringValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createStringValueExpression(MDataColumn mDataColumn, Object targetValue) {
//		String wrapper = mDataColumn.getWrapper();
		StringValue valueExpression = new StringValue("''");
		if (null != targetValue) {
			valueExpression = new StringValue("'" + setConditionValue(mDataColumn, targetValue) + "'");
//			if (StringUtils.isNotBlank(wrapper)) {
//				valueExpression = new WrapperExpression(wrapper, valueExpression);
//			}
		} else {
			LOGGER.warn("the expression value is empty, default value '' will be used.");
		}
		return valueExpression;
	}
	
	/**
	 * create DoubleValue expression instance.
	 * 
	 * @param valueKey
	 * @param dataPrivilegeParameter
	 * @return
	 * DoubleValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createDoubleValueExpression(MDataColumn mDataColumn, DataPrivilegeParameter dataPrivilegeParameter) {
		return createDoubleValueExpression(mDataColumn, dataPrivilegeParameter.getPrivilegeData(mDataColumn));
	}
	
	/**
	 * create DoubleValue expression instance.
	 * 
	 * @param valueKey
	 * @param targetValue
	 * @return
	 * DoubleValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createDoubleValueExpression(MDataColumn mDataColumn, Object targetValue) {
//		String wrapper = mDataColumn.getWrapper();
		Expression valueExpression = new DoubleValue("0");
		if (null != targetValue) {
			valueExpression = new DoubleValue(setConditionValue(mDataColumn, targetValue));
//			if (StringUtils.isNotBlank(wrapper)) {
//				valueExpression = new WrapperExpression(wrapper, valueExpression);
//			}
		} else {
			LOGGER.warn("the expression value is empty, default value 0d will be used.");
		}
		return valueExpression;
	}
	
	/**
	 * create LongValue expression instance.
	 * 
	 * @param valueKey
	 * @param dataPrivilegeParameter
	 * @return
	 * LongValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createLongValueExpression(MDataColumn mDataColumn, DataPrivilegeParameter dataPrivilegeParameter) {
		return createLongValueExpression(mDataColumn, dataPrivilegeParameter.getPrivilegeData(mDataColumn));
	}
	
	/**
	 * create LongValue expression instance.
	 * 
	 * @param valueKey
	 * @param targetValue
	 * @return
	 * LongValue
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createLongValueExpression(MDataColumn mDataColumn, Object targetValue) {
//		String wrapper = mDataColumn.getWrapper();
		Expression valueExpression = new LongValue("0");
		if (null != targetValue) {
			valueExpression = new DoubleValue(setConditionValue(mDataColumn, targetValue));
//			if (StringUtils.isNotBlank(wrapper)) {
//				valueExpression = new WrapperExpression(wrapper, valueExpression);
//			}
		} else {
			LOGGER.warn("the expression value is empty, default value 0l will be used.");
		}
		return valueExpression;
	}
	
	/**
	 * set condition value.
	 * 
	 * @param mDataColumn
	 * @param targetValue
	 * @return
	 * String
	 * @author Naughty Guo 2016年7月13日
	 */
	private static String setConditionValue(MDataColumn mDataColumn, Object targetValue) {
		String result = targetValue.toString();
		OperationType operationType = mDataColumn.getOperationType();
		if (operationType == OperationType.FULL_LIKE) {
			result = "%" + result + "%";
		} else if (operationType == OperationType.LEFT_LIKE) {
			result = "%" + result;
		} else if (operationType == OperationType.RIGHT_LIKE) {
			result = result + "%";
		}
		return result;
	}
	
	/**
	 * create basic value expression.
	 * 
	 * @param mDataColumn
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createValueExpression(MDataColumn mDataColumn, DataPrivilegeParameter dataPrivilegeParameter) {
		Expression expression = null;
		if (mDataColumn.getColumnType() == ColumnType.String) {
			expression = createStringValueExpression(mDataColumn, dataPrivilegeParameter);
		} else if (mDataColumn.getColumnType() == ColumnType.Double) {
			expression = createDoubleValueExpression(mDataColumn, dataPrivilegeParameter);
		} else if (mDataColumn.getColumnType() == ColumnType.Long) {
			expression = createLongValueExpression(mDataColumn, dataPrivilegeParameter);
		}
		return expression;
	}
	
	/**
	 * create basic value expression.
	 * 
	 * @param mDataColumn
	 * @param value
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createValueExpression(MDataColumn mDataColumn, String targetValue) {
		Expression expression = null;
		if (mDataColumn.getColumnType() == ColumnType.String) {
			expression = createStringValueExpression(mDataColumn, targetValue);
		} else if (mDataColumn.getColumnType() == ColumnType.Double) {
			expression = createDoubleValueExpression(mDataColumn, targetValue);
		} else if (mDataColumn.getColumnType() == ColumnType.Long) {
			expression = createLongValueExpression(mDataColumn, targetValue);
		}
		return expression;
	}
	
	/**
	 * create column expression.
	 * 
	 * @param mDataColumn
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createColumnExpression(MDataColumn mDataColumn) {
		return createColumnExpression(mDataColumn.getObject().getAlias(), mDataColumn.getName());
	}
	
	/**
	 * create column expression.
	 * 
	 * @param aliasName
	 * @param columnName
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月12日
	 */
	public static Expression createColumnExpression(String aliasName, String columnName) {
		Column expression = new Column();
		expression.setTable(TableFactory.createTable(aliasName));
		expression.setColumnName(columnName);
		return expression;
	}
	
	/**
	 * create single expression.
	 * 
	 * @param mDataCondition
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月14日
	 */
	public static Expression createSingleExpressioin(MDataCondition mDataCondition, DataPrivilegeParameter dataPrivilegeParameter) {
		return createExpression(mDataCondition.getColumns(), dataPrivilegeParameter);
	}

	/**
	 * create single expression.
	 * 
	 * @param mDataCondition
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月14日
	 */
	public static Expression createSingleJoinExpressioin(MDataJoinCondition mDataJoinCondition, DataPrivilegeParameter dataPrivilegeParameter) {
		return createExpression(mDataJoinCondition.getColumns(), dataPrivilegeParameter);
	}
	
	/**
	 * create expression.
	 * 
	 * @param mDataColumn
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月16日
	 */
	private static Expression createExpression(MDataColumn[] mDataColumns, DataPrivilegeParameter dataPrivilegeParameter) {
		Expression expression = null, middleExpression = null;
		for (MDataColumn mDataColumn : mDataColumns) {
			if (dataPrivilegeParameter.doConditionable(mDataColumn)) {
				if (mDataColumn.getOperationType() != OperationType.IN) {
					middleExpression = ExpressionFactory.createBinaryExpression(mDataColumn);
					BinaryExpression binaryExpression = (BinaryExpression) middleExpression;
					binaryExpression.setLeftExpression(ExpressionFactory.createColumnExpression(mDataColumn));
					binaryExpression.setRightExpression(ExpressionFactory.createValueExpression(mDataColumn, dataPrivilegeParameter));
				} else {
					if (null != dataPrivilegeParameter.getPrivilegeData(mDataColumn)) {
						middleExpression = createInExpression(mDataColumn, dataPrivilegeParameter);
					}
				}
				if (null != expression) {
					expression = ExpressionFactory.createCombineExpression(CombineType.AND, expression, middleExpression);
				} else {
					expression = middleExpression;
				}
			}
		}
		return expression;
	}
	
	/**
	 * create group expression.
	 * 
	 * @param categoryGroup
	 * @param mDataCondition
	 * @param dataPrivilegeParameter
	 * @return
	 * Expression
	 * @author Naughty Guo 2016年7月14日
	 */
	@SuppressWarnings("unchecked")
	public static Expression generateGroupExpression(MDataCondition mDataCondition, DataPrivilegeParameter dataPrivilegeParameter) {
		Expression expression = null, middleExpression = null;
		String categoryGroup = mDataCondition.getCategoryGroup();
		CombineType groupCombineType = mDataCondition.getGroupCombineType();
		DataPrivilegeParameter copyDataPrivilegeParameter = dataPrivilegeParameter.copyInstance();
		Object targetValue = dataPrivilegeParameter.getPrivilegeData().get(categoryGroup);
		if (null != targetValue && (targetValue instanceof Collection)) {
			List<Map<String, Object>> privilegeData = (List<Map<String, Object>>) targetValue;
			for (int i = 0; i < privilegeData.size(); i++) {
				copyDataPrivilegeParameter.setPrivilegeData(privilegeData.get(i));
				middleExpression = ExpressionFactory.createParenthesis(createSingleExpressioin(mDataCondition, copyDataPrivilegeParameter));
				if (null != expression) {
					expression = createCombineExpression(groupCombineType, expression, middleExpression);
				} else {
					expression = middleExpression;
				}
			}
		}
		return expression;
	}
	
	/**
	 * create InExpression.
	 * 
	 * @param mDataColumn
	 * @param dataPrivilegeParameter
	 * @return
	 * InExpression
	 * @author Naughty Guo 2016年7月15日
	 */
	public static InExpression createInExpression(MDataColumn mDataColumn, DataPrivilegeParameter dataPrivilegeParameter) {
		InExpression expression = new InExpression();
		expression.setLeftExpression(ExpressionFactory.createColumnExpression(mDataColumn));
		ExpressionList expressionList = new ExpressionList();
		List<Expression> expressions = new ArrayList<Expression>();
		expressionList.setExpressions(expressions);
		expression.setRightItemsList(expressionList);
		Object targetValue = dataPrivilegeParameter.getPrivilegeData(mDataColumn);
		if (null != targetValue) {
			for (String item : String.valueOf(targetValue).split(",")) {
				expressions.add(createValueExpression(mDataColumn, item));
			}
		}
		return expression;
	}
	
	/**
	 * create combine expression.
	 * 
	 * @param combineType
	 * @param leftExpression
	 * @param rightExpression
	 * @return
	 * @return Expression
	 * @author Naughty Guo Jun 11, 2016
	 */
	public static Expression createCombineExpression(CombineType combineType, Expression leftExpression, Expression rightExpression) {
		Expression expression = null;
		if (null == leftExpression && null == rightExpression) {
			return expression;
		}
		if (null == leftExpression) {
			return rightExpression;
		}
		if (null == rightExpression) {
			return leftExpression;
		}
		if (combineType == CombineType.AND) {
			expression = new AndExpression(leftExpression, rightExpression);
		} else if (combineType == CombineType.OR) {
			expression = new OrExpression(leftExpression, rightExpression);
		}
		return expression;
	}
	
	/**
	 * create Parenthesis expression.
	 *
	 * @param expression
	 * @return
	 * @return Expression
	 * @author Naughty Guo 
	 * @date Mar 8, 2018
	 */
	public static Expression createParenthesis(Expression expression) {
		if (null == expression) {
			return null;
		}
		if (expression instanceof Parenthesis) {
			return expression;
		}
		return new Parenthesis(expression);
	}
}
