package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.domain.sms.MessageDeliverLog;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsMessageDeliverLogMapper;

/**
 * 下行短信回执日志ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMessageDeliverLogService extends BaseService<SmsMessageDeliverLogMapper, MessageDeliverLog> {

}