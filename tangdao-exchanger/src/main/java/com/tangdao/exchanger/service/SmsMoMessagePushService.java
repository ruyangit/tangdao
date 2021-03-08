package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsMoMessagePushMapper;
import org.tangdao.modules.sms.model.domain.SmsMoMessagePush;
import org.tangdao.modules.sms.service.ISmsMoPushService;

/**
 * 上行消息推送ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMoMessagePushService extends CrudService<SmsMoMessagePushMapper, SmsMoMessagePush> implements ISmsMoPushService{
		
}