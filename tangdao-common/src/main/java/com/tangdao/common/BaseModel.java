/**
 *
 */
package com.tangdao.common;

import java.io.Serializable;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
public class BaseModel implements Serializable {

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

}
