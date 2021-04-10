/**
 *
 */
package com.tangdao.core.config;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月24日
 */
public class Global {

	// YES-0
	public static final String YES = "0";

	// NO-1
	public static final String NO = "1";

	// true
	public static final Boolean TRUE = true;

	// false
	public static final Boolean FALSE = false;

	// 0
	public static final String ROOT_ID = "0";

	/***
	 * 默认字段名定义
	 */
	public enum FieldName {
		/**
		 * 主键属性名
		 */
		id,
		/**
		 * 
		 */
		pid,
		/**
		 * 
		 */
		pids,
		/**
		 * 状态
		 */
		status,
		/**
		 * 创建时间
		 */
		createDate,
		/**
		 * 修改时间
		 */
		updateDate,
		/**
		 * 创建人
		 */
		createBy,
		/**
		 * 修改人
		 */
		updateBy
	}
}
