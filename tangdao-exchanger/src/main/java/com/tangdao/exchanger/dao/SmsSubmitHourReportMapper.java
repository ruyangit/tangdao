package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.SubmitHourReport;

/**
 * 提交报告（小时）Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsSubmitHourReportMapper extends BaseMapper<SubmitHourReport> {

}