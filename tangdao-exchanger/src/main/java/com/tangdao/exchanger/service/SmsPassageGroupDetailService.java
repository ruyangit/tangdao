package com.tangdao.exchanger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tangdao.core.dao.SmsPassageGroupDetailMapper;
import com.tangdao.core.model.domain.PassageGroupDetail;
import com.tangdao.core.service.BaseService;

/**
 * 通道组内容ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageGroupDetailService extends BaseService<SmsPassageGroupDetailMapper, PassageGroupDetail> {

	public List<String> findGroupIdByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.baseMapper.findGroupIdByPassageId(passageId);
	}

	public List<PassageGroupDetail> findPassageByGroupId(String passageGroupId) {
		return this.baseMapper.findPassageByGroupId(passageGroupId);
	}

}