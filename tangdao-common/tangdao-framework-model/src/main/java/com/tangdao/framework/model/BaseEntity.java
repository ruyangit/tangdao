package com.tangdao.framework.model;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;

import cn.hutool.core.util.ReflectUtil;

public abstract class BaseEntity <T extends Model<?>> extends Model<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志服务
	 */
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 主键
	 */
	@TableField(exist = false)
	protected String pkValue;

	/**
	 * 对应属性
	 */
	@JsonIgnore
	@TableField(exist = false)
	protected String pkFieldName;

	/**
	 * 对应列名
	 */
	@JsonIgnore
	@TableField(exist = false)
	protected String pkColumnName;
	
	/**
	 * @return the pkValue
	 */
	public String getPkValue() {
		if(StringUtils.isNotBlank(pkValue)) {
			return pkValue;
		}
		TableInfo tableInfo = TableInfoHelper.getTableInfo(getClass());
		if(tableInfo==null) {
			log.warn("未获取到对象，信息 {}", getClass());
			return null;
		}
		setPkValue((String)ReflectUtil.getFieldValue(getClass(), tableInfo.getKeyProperty()));
		return pkValue;
	}

	/**
	 * @param pkValue the pkValue to set
	 */
	public void setPkValue(String pkValue) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(getClass());
		if(tableInfo==null) {
			log.warn("未获取到对象，信息 {}", getClass());
			return;
		}
		this.setPkFieldName(tableInfo.getKeyProperty());
		this.setPkColumnName(tableInfo.getKeyColumn());
		this.pkValue = pkValue;
	}

	/**
	 * @return the pkFieldName
	 */
	public String getPkFieldName() {
		return pkFieldName;
	}

	/**
	 * @param pkFieldName the pkFieldName to set
	 */
	public void setPkFieldName(String pkFieldName) {
		this.pkFieldName = pkFieldName;
	}

	/**
	 * @return the pkColumnName
	 */
	public String getPkColumnName() {
		return pkColumnName;
	}

	/**
	 * @param pkColumnName the pkColumnName to set
	 */
	public void setPkColumnName(String pkColumnName) {
		this.pkColumnName = pkColumnName;
	}
	
}
