package com.tangdao.exchanger.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.ForbiddenWords;

/**
 * 告警敏感词Mapper接口
 * 
 * @author ruyang
 * @version 2019-09-06
 */
@Mapper
public interface SmsForbiddenWordsMapper extends BaseMapper<ForbiddenWords> {

	public List<String> selectAllWords();
}