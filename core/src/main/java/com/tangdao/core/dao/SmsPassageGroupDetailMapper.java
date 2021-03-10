package com.tangdao.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.PassageGroupDetail;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface SmsPassageGroupDetailMapper extends BaseMapper<PassageGroupDetail> {

	@Select("select distinct group_id from sms_passage_group_detail where passage_id =#{passageId}")
	public List<String> findGroupIdByPassageId(String passageId);

	public List<PassageGroupDetail> findPassageByGroupId(String passageGroupId);
}