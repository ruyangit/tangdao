package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMtProcessFailedMapper;
import org.tangdao.modules.sms.model.domain.SmsMtProcessFailed;
import org.tangdao.modules.sms.service.ISmsMtProcessFailedService;

/**
 * 下行短信处理失败ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMtProcessFailedService extends CrudService<SmsMtProcessFailedMapper, SmsMtProcessFailed> implements ISmsMtProcessFailedService{
		
}