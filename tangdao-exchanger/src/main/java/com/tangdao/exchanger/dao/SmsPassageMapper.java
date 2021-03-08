package com.tangdao.exchanger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.Passage;

/**
 * 通道管理Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageMapper extends BaseMapper<Passage> {

	@Select("select distinct(code) from sms_passage")
	public List<String> selectAvaiableCodes();

	@Select("select s.id, s.status, s.create_time from sms_passage s where s.code = #{code} limit 1")
	public Passage getPassageByCode(@Param("code") String code);
}