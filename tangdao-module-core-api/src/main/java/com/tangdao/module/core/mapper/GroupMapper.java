package com.tangdao.module.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.tangdao.module.core.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户组表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-03-11
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
