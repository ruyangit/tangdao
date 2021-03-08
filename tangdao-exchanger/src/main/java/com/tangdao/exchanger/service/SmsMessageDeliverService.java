package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMessageDeliverMapper;
import org.tangdao.modules.sms.model.domain.SmsMessageDeliver;
import org.tangdao.modules.sms.service.ISmsDeliverService;

/**
 * 回执推送信息ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMessageDeliverService extends CrudService<SmsMessageDeliverMapper, SmsMessageDeliver>
		implements ISmsDeliverService {

}