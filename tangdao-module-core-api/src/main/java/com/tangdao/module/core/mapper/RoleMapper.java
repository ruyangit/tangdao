package com.tangdao.module.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tangdao.framework.model.RoleInfo;
import com.tangdao.module.core.model.domain.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

	/**
	 * 获取用户角色
	 * @param role
	 * @return
	 */
	List<RoleInfo> listRoleInfo(RoleInfo role);
}
