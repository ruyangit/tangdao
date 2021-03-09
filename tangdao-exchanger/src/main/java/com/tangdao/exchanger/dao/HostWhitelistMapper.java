package com.tangdao.exchanger.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.HostWhitelist;

@Mapper
public interface HostWhitelistMapper extends BaseMapper<HostWhitelist> {

}
