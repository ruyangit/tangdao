package com.tangdao.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.model.domain.UserBalanceLog;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {

	public Page<UserBalanceLog> findUserBalanceLogPage(Page<UserBalanceLog> page, UserBalanceLog userBalanceLog);
}