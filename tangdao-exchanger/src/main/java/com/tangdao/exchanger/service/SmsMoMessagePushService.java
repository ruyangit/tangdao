package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.domain.sms.MoMessagePush;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsMoMessagePushMapper;

/**
 * 上行消息推送ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsMoMessagePushService extends BaseService<SmsMoMessagePushMapper, MoMessagePush> {

}