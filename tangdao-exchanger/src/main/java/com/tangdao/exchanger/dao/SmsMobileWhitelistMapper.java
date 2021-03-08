package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.sms.MobileWhitelist;

/**
 * 手机白名单信息表Mapper接口
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsMobileWhitelistMapper extends BaseMapper<MobileWhitelist> {
	
}