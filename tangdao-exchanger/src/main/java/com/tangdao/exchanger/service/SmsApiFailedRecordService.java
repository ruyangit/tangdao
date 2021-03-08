package com.tangdao.exchanger.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.domain.sms.ApiFailedRecord;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsApiFailedRecordMapper;

/**
 * 下行失败短信ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsApiFailedRecordService extends BaseService<SmsApiFailedRecordMapper, ApiFailedRecord> {

}