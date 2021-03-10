package com.tangdao.core.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.dao.SmsPassageParameterMapper;
import com.tangdao.core.model.domain.SmsPassageParameter;
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
public class SmsPassageParameterService extends BaseService<SmsPassageParameterMapper, SmsPassageParameter> {

	public List<SmsPassageParameter> findByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.list(Wrappers.<SmsPassageParameter>lambdaQuery().eq(SmsPassageParameter::getPassageId, passageId));
	}

	public SmsPassageParameter getByType(PassageCallType callType, String passageCode) {
		// TODO Auto-generated method stub
		return this.getOne(Wrappers.<SmsPassageParameter>lambdaQuery().eq(SmsPassageParameter::getUrl, passageCode)
				.eq(SmsPassageParameter::getCallType, callType.getCode()));
	}

	public boolean deleteByPassageId(String passageId) {
		// TODO Auto-generated method stub
		return this.remove(Wrappers.<SmsPassageParameter>lambdaQuery().eq(SmsPassageParameter::getPassageId, passageId));
	}

	public SmsPassageParameter selectSendProtocol(String passageId) {
		QueryWrapper<SmsPassageParameter> queryWrapper = new QueryWrapper<SmsPassageParameter>();
		queryWrapper.eq("passage_id", passageId);
		queryWrapper.eq("call_type", 1);
		queryWrapper.orderByDesc("id").last(" limit 1");
		return this.getOne(queryWrapper);
	}

}