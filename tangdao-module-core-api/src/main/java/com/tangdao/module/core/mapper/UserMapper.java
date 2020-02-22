package com.tangdao.module.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tangdao.module.core.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
