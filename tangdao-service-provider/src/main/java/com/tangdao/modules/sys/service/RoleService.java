/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.Role;
import com.tangdao.model.domain.UserRole;
import com.tangdao.model.dto.RoleDTO;
import com.tangdao.modules.sys.mapper.RoleMapper;
import com.tangdao.modules.sys.mapper.UserRoleMapper;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月2日
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> {

	@Autowired
	private UserRoleMapper userRoleMapper;

	public Role findByRoleName(String roleName) {
		return findRole(this.list(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, roleName)));
	}

	public Role findRole(List<Role> roles) {
		if (CollUtil.isNotEmpty(roles)) {
			return roles.get(0);
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteRole(String id) {
		this.userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, id));
		return super.removeById(id);
	}

	/**
	 * TODO 检查角色信息是否被引用
	 * 
	 * @param roleDto
	 * @return true 引用，false 未引用
	 */
	public boolean checkUserRoleRef(RoleDTO roleDto) {
		return this.userRoleMapper
				.selectCount(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getRoleId, roleDto.getId())) > 0;
	}
}
