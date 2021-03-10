package com.tangdao.core.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.SmsForbiddenWords;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface SmsForbiddenWordsMapper extends BaseMapper<SmsForbiddenWords> {

	public List<String> selectAllWords();
}