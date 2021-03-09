package com.tangdao.exchanger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.PassageGroupDetail;

/**
 * 通道组内容Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageGroupDetailMapper extends BaseMapper<PassageGroupDetail> {

	@Select("select distinct group_id from sms_passage_group_detail where passage_id =#{passageId}")
	public List<String> findGroupIdByPassageId(String passageId);

	public List<PassageGroupDetail> findPassageByGroupId(String passageGroupId);
}