package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.UserBalance;

@Mapper
public interface UserBalanceMapper extends BaseMapper<UserBalance> {

	public int updateWarning(UserBalance userBalance);
}
