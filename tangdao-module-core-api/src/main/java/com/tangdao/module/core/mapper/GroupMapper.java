package com.tangdao.module.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.module.core.model.domain.Group;

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

	public List<Group> listGroupsForUser(String userId);
}
