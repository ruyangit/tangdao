package com.tangdao.core.dao;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.SmsPassageArea;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface SmsPassageAreaMapper extends BaseMapper<SmsPassageArea> {

	public int deleteByPassageId(@Param("passageId") String passageId);
}