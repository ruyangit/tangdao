package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.paas.Area;

/**
 * 行政区划Mapper接口
 * @author ruyang
 * @version 2019-09-27
 */
@Mapper
public interface AreaMapper extends BaseMapper<Area> {
	
}