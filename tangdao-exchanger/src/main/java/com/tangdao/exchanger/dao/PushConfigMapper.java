package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.paas.PushConfig;

@Mapper
public interface PushConfigMapper extends BaseMapper<PushConfig> {
	
	public int updateByUserCode(PushConfig pushConfig);
}