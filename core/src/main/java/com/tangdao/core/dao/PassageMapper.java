package com.tangdao.core.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.Passage;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface PassageMapper extends BaseMapper<Passage> {

	@Select("select distinct(code) from sms_passage")
	public List<String> selectAvaiableCodes();

	@Select("select s.id, s.status, s.create_time from sms_passage s where s.code = #{code} limit 1")
	public Passage getPassageByCode(@Param("code") String code);
}