package org.tangdao.modules.sms.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsPassageChangeLogMapper;
import org.tangdao.modules.sms.model.domain.SmsPassageChangeLog;
import org.tangdao.modules.sms.service.ISmsPassageChangeLogService;

/**
 * 通道变更日志ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageChangeLogServiceImpl extends CrudService<SmsPassageChangeLogMapper, SmsPassageChangeLog> implements ISmsPassageChangeLogService{
		
}