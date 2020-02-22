package com.tangdao.module.core.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.module.core.model.domain.Group;

/**
 * <p>
 * 用户组 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}
