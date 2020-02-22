package com.tangdao.framework.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;

public abstract class BaseEntity <T extends Model<?>> extends Model<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日志服务
	 */
	@TableField(exist = false)
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * 无参构造
	 */
	public BaseEntity() {
	}
}
