package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.MessageTemplate;

/**
 * 消息模板Mapper接口
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsMessageTemplateMapper extends BaseMapper<MessageTemplate> {
	
}