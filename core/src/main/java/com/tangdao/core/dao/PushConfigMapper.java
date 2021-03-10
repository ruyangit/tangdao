package com.tangdao.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.PushConfig;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface PushConfigMapper extends BaseMapper<PushConfig> {
	
	public int updateByUserId(PushConfig pushConfig);
}