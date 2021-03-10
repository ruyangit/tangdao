package com.tangdao.core.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.MobileWhitelist;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface SmsMobileWhitelistMapper extends BaseMapper<MobileWhitelist> {

	/**
	 * 
	 * TODO 根据用户ID查询手机号码集合（去重复）
	 * 
	 * @param userId
	 * @return
	 */
	List<String> selectDistinctMobilesByUserId(String userId);
}