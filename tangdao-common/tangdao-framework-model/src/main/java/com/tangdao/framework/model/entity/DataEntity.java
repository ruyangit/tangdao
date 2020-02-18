package com.tangdao.framework.model.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;

public abstract class DataEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 基础字段
	@TableField(fill = FieldFill.INSERT)
	protected String createBy; // 新增人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	protected Date createTime; // 新增时间

	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected String updateBy; // 更新人

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Date updateTime; // 更新时间

	@TableField(fill = FieldFill.INSERT)
	protected String status; // 状态

	protected String remarks; // 备注
	
	/**
	 * 正常
	 */
	public static final String STATUS_NORMAL = "0";
	/**
	 * 已删除
	 */
	public static final String STATUS_DELETE = "1";
	/**
	 * 停用
	 */
	public static final String STATUS_DISABLE = "2";
	/**
	 * 冻结
	 */
	public static final String STATUS_FREEZE = "3";
	

	public DataEntity() {
		
	}
}
