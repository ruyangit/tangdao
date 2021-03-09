package com.tangdao.exchanger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.MobileWhitelist;

/**
 * 手机白名单信息表Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsMobileWhitelistMapper extends BaseMapper<MobileWhitelist> {

	/**
	 * 
	 * TODO 根据用户ID查询手机号码集合（去重复）
	 * 
	 * @param userCode
	 * @return
	 */
	List<String> selectDistinctMobilesByUserCode(String userCode);
}