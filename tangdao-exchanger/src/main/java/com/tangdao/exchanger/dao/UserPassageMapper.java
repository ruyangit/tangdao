package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.paas.UserPassage;

@Mapper
public interface UserPassageMapper extends BaseMapper<UserPassage> {
	
	public int updateByUserCodeAndType(@Param("passageGroupId") String passageGroupId, @Param("userCode") String userCode, @Param("type") Integer type);
}