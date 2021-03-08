package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.model.domain.paas.UserBalanceLog;

@Mapper
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {
	
    public Page<UserBalanceLog> findUserBalanceLogPage(Page<UserBalanceLog> page, UserBalanceLog userBalanceLog);
}