package com.tangdao.exchanger.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.model.domain.sms.PassageParameter;
import com.tangdao.core.service.BaseService;
import com.tangdao.exchanger.dao.SmsPassageParameterMapper;

/**
 * 通道消息模板参数ServiceImpl
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Service
public class SmsPassageParameterService extends BaseService<SmsPassageParameterMapper, PassageParameter> {

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