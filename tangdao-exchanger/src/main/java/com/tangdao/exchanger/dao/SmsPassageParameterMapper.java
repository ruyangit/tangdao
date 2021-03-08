package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.PassageParameter;

/**
 * 通道消息模板参数Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageParameterMapper extends BaseMapper<PassageParameter> {

}