package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMtManualHandlingMapper;
import org.tangdao.modules.sms.model.domain.SmsMtManualHandling;
import org.tangdao.modules.sms.service.ISmsMtManualHandlingService;

/**
 * 下行短信创建ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtManualHandlingService extends CrudService<SmsMtManualHandlingMapper, SmsMtManualHandling>
		implements ISmsMtManualHandlingService {

}