/**
 *
 */
package com.tangdao.core.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
@Getter
@Setter
public abstract class BaseModel implements Serializable {
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
	/**
	 * 主键名
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String primaryKey;
	/**
	 * 主键值
	 */
	@JsonIgnore
	@TableField(exist = false)
	private String primaryKeyVal;

}
