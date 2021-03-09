package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.PassageReachrateSettings;

/**
 * 通道重连配置Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsPassageReachrateSettingsMapper extends BaseMapper<PassageReachrateSettings> {

}