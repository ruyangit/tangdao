package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.PassageChangeLog;

/**
 * 通道变更日志Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageChangeLogMapper extends BaseMapper<PassageChangeLog> {

}