package org.tangdao.modules.sys.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.common.suports.Page;
import org.tangdao.modules.sys.mapper.UserBalanceLogMapper;
import org.tangdao.modules.sys.model.domain.Customer;
import org.tangdao.modules.sys.model.domain.UserBalanceLog;
import org.tangdao.modules.sys.model.vo.CustomerUser;
import org.tangdao.modules.sys.service.IUserBalanceLogService;

/**
 * 用户余额日志ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class UserBalanceLogServiceImpl extends CrudService<UserBalanceLogMapper, UserBalanceLog> implements IUserBalanceLogService{
    @Override
    public Page<UserBalanceLog> findUserBalanceLogPage(Page<UserBalanceLog> page, UserBalanceLog userBalanceLog) {
        return super.baseMapper.findUserBalanceLogPage(page, userBalanceLog);
    }
}