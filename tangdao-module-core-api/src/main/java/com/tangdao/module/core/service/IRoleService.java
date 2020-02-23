package com.tangdao.module.core.service;

import java.util.List;

import com.tangdao.framework.model.RoleVo;
import com.tangdao.framework.service.ICrudService;
import com.tangdao.module.core.model.domain.Role;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
public interface IRoleService extends ICrudService<Role> {

	/**
	 * 查询角色权限
	 * @param userId
	 * @return
	 */
	public List<RoleVo> findRoleVoList(String userId);
}
