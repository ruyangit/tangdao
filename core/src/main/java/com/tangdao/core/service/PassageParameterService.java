package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.dao.PassageParameterMapper;
import com.tangdao.core.model.domain.PassageParameter;
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
public class PassageParameterService extends BaseService<PassageParameterMapper, PassageParameter> {

	public List<PassageParameter> findByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.list(Wrappers.<PassageParameter>lambdaQuery().eq(PassageParameter::getPassageId, passageId));
	}

	public PassageParameter getByType(PassageCallType callType, String passageCode) {
		// TODO Auto-generated method stub
		return this.getOne(Wrappers.<PassageParameter>lambdaQuery().eq(PassageParameter::getUrl, passageCode)
				.eq(PassageParameter::getCallType, callType.getCode()));
	}

	public boolean deleteByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.remove(Wrappers.<PassageParameter>lambdaQuery().eq(PassageParameter::getPassageId, passageId));
	}

	public PassageParameter selectSendProtocol(String passageId) {
		QueryWrapper<PassageParameter> queryWrapper = new QueryWrapper<PassageParameter>();
		queryWrapper.eq("passage_id", passageId);
		queryWrapper.eq("call_type", 1);
		queryWrapper.orderByDesc("id").last(" limit 1");
		return this.getOne(queryWrapper);
	}

}