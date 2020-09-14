/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.Role;
import com.tangdao.model.domain.RoleMenu;
import com.tangdao.model.domain.RolePolicy;
import com.tangdao.model.domain.UserRole;
import com.tangdao.model.dto.PolicyDTO;
import com.tangdao.model.dto.RoleDTO;
import com.tangdao.modules.sys.mapper.RoleMapper;
import com.tangdao.modules.sys.mapper.RoleMenuMapper;
import com.tangdao.modules.sys.mapper.RolePolicyMapper;
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
	
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	
	@Autowired
	private RolePolicyMapper rolePolicyMapper;
	
	@Autowired
	private CacheService cacheService;

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
	
	@Transactional(rollbackFor = Exception.class)
	public boolean saveRoleMenu(RoleDTO roleDTO) {
		this.roleMenuMapper.delete(Wrappers.<RoleMenu>lambdaQuery().eq(RoleMenu::getRoleId, roleDTO.getId()));
		roleDTO.getMenuIds().forEach(id->{
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRoleId(roleDTO.getId());
			roleMenu.setMenuId(id);
			this.roleMenuMapper.insert(roleMenu);
		});
		this.cacheService.clear(CacheService.RED_USER_MENU);
		return true;
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
	
	public List<RolePolicy> findRolePolicy(String roleId){
		QueryWrapper<RolePolicy> queryWrapper = new QueryWrapper<RolePolicy>();
		queryWrapper.eq("role_id", roleId);
		return this.rolePolicyMapper.selectList(queryWrapper);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean saveRolePolicy(PolicyDTO policyDTO) {
		this.rolePolicyMapper.delete(Wrappers.<RolePolicy>lambdaQuery().eq(RolePolicy::getRoleId, policyDTO.getRoleId()));
		policyDTO.getPolicyIds().forEach(id->{
			RolePolicy rolePolicy = new RolePolicy();
			rolePolicy.setRoleId(policyDTO.getRoleId());
			rolePolicy.setPolicyId(id);
			this.rolePolicyMapper.insert(rolePolicy);
		});
		this.cacheService.clear(CacheService.RED_USER_POLICY_STATEMENTS);
		return true;
	}
}
