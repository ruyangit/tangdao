package com.tangdao.core.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.UserPassage;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface UserPassageMapper extends BaseMapper<UserPassage> {

	public int updateByUserIdAndType(@Param("passageGroupId") String passageGroupId, @Param("userId") String userId,
			@Param("type") Integer type);
}