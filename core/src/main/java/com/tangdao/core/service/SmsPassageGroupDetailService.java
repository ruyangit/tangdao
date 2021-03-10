package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tangdao.core.dao.SmsPassageGroupDetailMapper;
import com.tangdao.core.model.domain.SmsPassageGroupDetail;
import com.tangdao.core.service.BaseService;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class SmsPassageGroupDetailService extends BaseService<SmsPassageGroupDetailMapper, SmsPassageGroupDetail> {

	public List<String> findGroupIdByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.baseMapper.findGroupIdByPassageId(passageId);
	}

	public List<SmsPassageGroupDetail> findPassageByGroupId(String passageGroupId) {
		return this.baseMapper.findPassageByGroupId(passageGroupId);
	}

}