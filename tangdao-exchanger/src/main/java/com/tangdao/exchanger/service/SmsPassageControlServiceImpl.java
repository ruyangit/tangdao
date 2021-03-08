package org.tangdao.modules.sms.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsPassageControlMapper;
import org.tangdao.modules.sms.model.domain.SmsPassageControl;
import org.tangdao.modules.sms.service.ISmsPassageControlService;

/**
 * 通道控制ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageControlServiceImpl extends CrudService<SmsPassageControlMapper, SmsPassageControl> implements ISmsPassageControlService{
		
}