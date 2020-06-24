package com.tangdao.core.mybatis.data.privilege.constant;

/**
 * 
 * @ClassName: DataPrivilegeConstant.java
 * @author: Naughty Guo
 * @date: Jun 2, 2016
 */
public interface DataPrivilegeConstant {
	
	/**
	 * enum CombineType.
	 * 
	 * @ClassName: CombineType.java
	 * @author: Naughty Guo
	 * @date: Jun 2, 2016
	 */
	public enum CombineType {
		AND,
		OR
	}
	
	/**
	 * enum OperationType.
	 * 
	 * @ClassName: OperationType.java
	 * @author: Naughty Guo
	 * @date: Jun 2, 2016
	 */
	public enum OperationType {
		EQUAL,
		NOT_EQUAL,
		GREATER_THAN,
		GREATER_THAN_EQUAL,
		MINOR_THAN,
		MINOR_THAN_EQUAL,
		IN,
		LEFT_LIKE,
		RIGHT_LIKE,
		FULL_LIKE
	}
	
	/**
	 * enum ColumnType.
	 * 
	 * @ClassName: ColumnType.java
	 * @author: Naughty Guo
	 * @date: Jun 2, 2016
	 */
	public enum ColumnType {
		String,
		Double,
		Long
	}

	/**
	 * enum ObjectJoinType.
	 * 
	 * @ClassName: ObjectJoinType.java
	 * @author: Naughty Guo
	 * @date: Jun 2, 2016
	 */
	public enum ObjectJoinType {
		LEFT,
		RIGHT,
		FULL,
		INNER,
		OUTER
	}
}
