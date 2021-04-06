/**
 *
 */
package com.tangdao.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.ChainWrappers;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
public abstract class BaseService<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IService<T> {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 构建mybatis-plus的query
	 */
	public QueryChainWrapper<T> query() {
		return ChainWrappers.queryChain(this.getBaseMapper());
	}

	/**
	 * 构建mybatis-plus的lambdaQuery
	 */
	public LambdaQueryChainWrapper<T> lambdaQuery() {
		return ChainWrappers.lambdaQueryChain(this.getBaseMapper());
	}

	/**
	 * 构建mybatis-plus的update
	 */
	public UpdateChainWrapper<T> update() {
		return ChainWrappers.updateChain(this.getBaseMapper());
	}

	/**
	 * 构建mybatis-plus的lambdaUpdate
	 */
	public LambdaUpdateChainWrapper<T> lambdaUpdate() {
		return ChainWrappers.lambdaUpdateChain(this.getBaseMapper());
	}

}
