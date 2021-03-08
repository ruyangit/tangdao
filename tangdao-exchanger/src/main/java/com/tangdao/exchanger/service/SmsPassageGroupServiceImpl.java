package org.tangdao.modules.sms.service;

import org.springframework.stereotype.Service;
import org.tangdao.common.service.CrudService;
import org.tangdao.modules.sms.mapper.SmsPassageGroupMapper;
import org.tangdao.modules.sms.model.domain.SmsPassageGroup;
import org.tangdao.modules.sms.service.ISmsPassageGroupService;

/**
 * 通道组ServiceImpl
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageGroupServiceImpl extends CrudService<SmsPassageGroupMapper, SmsPassageGroup> implements ISmsPassageGroupService{
		
}