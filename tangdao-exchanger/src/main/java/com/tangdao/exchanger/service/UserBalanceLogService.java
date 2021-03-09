package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.model.domain.UserBalanceLog;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.UserBalanceLogMapper;

/**
 * 用户余额日志ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class UserBalanceLogService extends BaseService<UserBalanceLogMapper, UserBalanceLog> {

	public Page<UserBalanceLog> findUserBalanceLogPage(Page<UserBalanceLog> page, UserBalanceLog userBalanceLog) {
		return super.baseMapper.findUserBalanceLogPage(page, userBalanceLog);
	}
}