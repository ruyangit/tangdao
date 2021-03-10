package com.tangdao.core.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.SmsMtMessageSubmit;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface SmsMtMessageSubmitMapper extends BaseMapper<SmsMtMessageSubmit> {

	List<SmsMtMessageSubmit> findList(Map<String, Object> queryParams);
}