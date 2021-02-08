/**
 *
 */
package com.tangdao.core;

import java.io.Serializable;
import java.util.LinkedHashMap;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
@Data
public abstract class BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 正常
	 */
	public static final String NORMAL = "0";
	/**
	 * 已删除
	 */
	public static final String DELETE = "1";
	/**
	 * 停用
	 */
	public static final String DISABLE = "2";
	/**
	 * 冻结
	 */
	public static final String FREEZE = "3";
	/**
	 * 审核
	 */
	public static final String AUDIT = "4";
	/**
	 * 回退
	 */
	public static final String AUDIT_BACK = "5";
	/**
	 * 草稿
	 */
	public static final String DRAFT = "9";

	private LinkedHashMap<String, Object> dataSqlMap;

	public void setDataSqlMap(String key, Object value) {
		this.getDataSqlMap().put(key, value);
	}
}