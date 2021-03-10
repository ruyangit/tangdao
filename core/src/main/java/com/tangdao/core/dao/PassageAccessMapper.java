package com.tangdao.core.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.PassageAccess;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface PassageAccessMapper extends BaseMapper<PassageAccess> {
	
	@Delete("delete from sms_passage_access where passage_id = ${passageId}")
	public int deleteByPasageId(String passageId);
	
	@Update("update sms_passage_access set status = #{status} where passage_id = ${passageId}")
	public int updateStatusByPassageId(String status, String passageId);
	
	@Delete("delete from sms_passage_access where user_id = ${userId}")
	public int deleteByUserId(String userId);
}