/**
 *
 */
package com.tangdao.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.dao.RoleMapper;
import com.tangdao.core.model.domain.Role;

/**
 * <p>
 * TODO 角色服务
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
public class RoleService extends BaseService<RoleMapper, Role> {

	/**
	 * 
	 * TODO 根据角色代码查询
	 * @param roleCode
	 * @return
	 */
	public Role getByRoleCode(String roleCode) {
		return super.getOne(Wrappers.<Role>lambdaQuery().eq(Role::getRoleCode, roleCode));
	}
	
	/**
	 * 
	 * TODO 检查角色代码是否存在
	 * @param oldRoleCode
	 * @param roleCode
	 * @return 存在-true，不存在-false
	 */
	public boolean checkRoleCodeExists(String oldRoleCode, String roleCode) {
		if (oldRoleCode != null && oldRoleCode.equals(roleCode)) {
			return true;
		} else if (roleCode != null && super.count(Wrappers.<Role>lambdaQuery().eq(Role::getRoleCode, roleCode)) == 0) {
			return true;
		}
		return false;
	}
}
