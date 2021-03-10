package com.tangdao.core.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.ForbiddenWords;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface ForbiddenWordsMapper extends BaseMapper<ForbiddenWords> {

	public List<String> selectAllWords();
}