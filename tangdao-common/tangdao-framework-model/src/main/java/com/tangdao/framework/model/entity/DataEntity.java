package com.tangdao.framework.model.entity;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.util.ReflectUtil;

public abstract class DataEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 创建人
	 */
	@TableField(fill = FieldFill.INSERT)
	protected String createBy;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	protected Date createTime;

	/**
	 * 更新人
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected String updateBy;

	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	protected Date updateTime;

	/**
	 * 状态
	 */
	@TableField(fill = FieldFill.INSERT)
	protected String status;

	/**
	 * 备注
	 */
	protected String remarks;
	
	/**
	 * 主键
	 */
	@TableField(exist = false)
	protected String key;

	/**
	 * 对应属性
	 */
	@JsonIgnore
	@TableField(exist = false)
	protected String keyAttrName;

	/**
	 * 对应列名
	 */
	@JsonIgnore
	@TableField(exist = false)
	protected String keyColumnName;

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

	/**
	 * 无参构造
	 */
	public DataEntity() {
		
	}
	
	/**
	 * 主键构造
	 * 
	 * @param key
	 */
	public DataEntity(String key) {
		if (key != null) {
			this.setKey(key);
		}
	}

	public String getCreateBy() {
		return createBy;
	}


	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getUpdateBy() {
		return updateBy;
	}


	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}


	public Date getUpdateTime() {
		return updateTime;
	}


	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getRemarks() {
		return remarks;
	}


	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	/**
	 * @return the keyAttrName
	 */
	public String getKeyAttrName() {
		return keyAttrName;
	}

	/**
	 * @param keyAttrName the keyAttrName to set
	 */
	public void setKeyAttrName(String keyAttrName) {
		this.keyAttrName = keyAttrName;
	}

	/**
	 * @return the keyColumnName
	 */
	public String getKeyColumnName() {
		return keyColumnName;
	}

	/**
	 * @param keyColumnName the keyColumnName to set
	 */
	public void setKeyColumnName(String keyColumnName) {
		this.keyColumnName = keyColumnName;
	}

	/**
	 * 获取主键
	 * 
	 * @return
	 */
	public String getKey() {
		if (StringUtils.isNotBlank(this.key)) {
			return this.key;
		}
		String value = null;
		try {
			TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getClass());
			if (tableInfo != null) {
				value = (String)ReflectUtil.getFieldValue(this, tableInfo.getKeyProperty());
			}
			if (StringUtils.isNotBlank(value)) {
				this.setKey(value);
			}
		} catch (Exception e) {}
		return value;
	}

	/**
	 * 主键信息
	 * 
	 * @param key
	 */
	public void setKey(String key) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(this.getClass());
		if (tableInfo != null) {
			this.setKeyAttrName(tableInfo.getKeyProperty());
			this.setKeyColumnName(tableInfo.getKeyColumn());
			ReflectUtil.setFieldValue(this, this.keyAttrName, key);
			this.key = key;
		}
	}
	
}
