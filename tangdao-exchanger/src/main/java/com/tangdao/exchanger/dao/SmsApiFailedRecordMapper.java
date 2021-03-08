package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.ApiFailedRecord;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Mapper
public interface SmsApiFailedRecordMapper extends BaseMapper<ApiFailedRecord> {
	
}