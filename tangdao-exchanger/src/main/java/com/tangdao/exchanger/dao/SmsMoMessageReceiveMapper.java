package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.MoMessageReceive;

/**
 * 上行消息回复Mapper接口
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsMoMessageReceiveMapper extends BaseMapper<MoMessageReceive> {
	
}