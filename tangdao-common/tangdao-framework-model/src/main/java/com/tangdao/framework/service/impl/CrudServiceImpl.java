package com.tangdao.framework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tangdao.framework.service.ICrudService;

public class CrudServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements ICrudService<T> {

	/**
	 * 日志服务
	 */
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	/**
	 * ModelClass Current
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Class<T> currentModelClass() {
		return (Class<T>) ReflectionKit.getSuperClassGenericType(getClass(), 1);
	}
}
