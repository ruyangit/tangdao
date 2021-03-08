package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.PassageArea;

/**
 * 通道支持省份Mapper接口
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageAreaMapper extends BaseMapper<PassageArea> {

	public int deleteByPassageId(@Param("passageId") String passageId);
}