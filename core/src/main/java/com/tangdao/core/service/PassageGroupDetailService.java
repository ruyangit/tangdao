package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tangdao.core.dao.PassageGroupDetailMapper;
import com.tangdao.core.model.domain.PassageGroupDetail;
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
public class PassageGroupDetailService extends BaseService<PassageGroupDetailMapper, PassageGroupDetail> {

	public List<String> findGroupIdByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.baseMapper.findGroupIdByPassageId(passageId);
	}

	public List<PassageGroupDetail> findPassageByGroupId(String passageGroupId) {
		return this.baseMapper.findPassageByGroupId(passageGroupId);
	}

}