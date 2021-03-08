package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMtMessageDeliverLogMapper;
import org.tangdao.modules.sms.model.domain.SmsMtMessageDeliverLog;
import org.tangdao.modules.sms.service.ISmsMtDeliverLogService;

/**
 * 下行短信回执状态日志ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtMessageDeliverLogService extends
		CrudService<SmsMtMessageDeliverLogMapper, SmsMtMessageDeliverLog> implements ISmsMtDeliverLogService {

}