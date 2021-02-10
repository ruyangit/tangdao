/**
 *
 */
package com.tangdao.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.system.mapper.RoleMapper;
import com.tangdao.system.model.domain.Role;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

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

	/**
	 * 
	 * TODO 根据用户编码查询角色信息
	 * 
	 * @param userCode
	 * @return
	 */
	public List<Role> findByUserCode(String userCode) {
		Assert.notEmpty(userCode, "异常：userCode 不可以为空");
		Role role = new Role();
		role.setUserCode(userCode);
		return baseMapper.findByUserRole(role);
	}

	/**
	 * 
	 * TODO 检查角色名称是否存在
	 * 
	 * @param oldRoleName
	 * @param roleName
	 * @return 是：true；否：false
	 */
	public boolean checkRoleNameExists(String oldRoleName, String roleName) {
		if (roleName != null && roleName.equals(oldRoleName)) {
			return true;
		} else if (roleName != null && this.count(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, roleName)) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * TODO 保存角色与菜单数据
	 * 
	 * @param role
	 * @param menuCodes
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean insertRoleMenu(Role role) {
		Assert.notEmpty(role.getRoleCode(), "异常：roleCode 不可以为空");
		this.baseMapper.deleteRoleMenu(role.getRoleCode());
		if (CollUtil.isNotEmpty(role.getMenuCodes()) && StrUtil.isNotBlank(role.getRoleCode())) {
			this.baseMapper.insertRoleMenu(role.getRoleCode(), role.getMenuCodes());
		}
		return true;
	}

	/**
	 * 
	 * TODO 保存角色与用户数据
	 * 
	 * @param roleCode
	 * @param userCodes
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean insertRoleUser(Role role) {
		Assert.notEmpty(role.getRoleCode(), "异常：roleCode 不可以为空");
		this.baseMapper.deleteRoleUser(role.getRoleCode());
		if (CollUtil.isNotEmpty(role.getUserCodes()) && StrUtil.isNotBlank(role.getRoleCode())) {
			this.baseMapper.insertRoleUser(role.getRoleCode(), role.getUserCodes());
		}
		return true;
	}
	
	/**
	 * 
	 * TODO
	 * @param role
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteRole(Role role) {
		Assert.notEmpty(role.getRoleCode(), "异常：roleCode 不可以为空");
		this.baseMapper.deleteRoleUser(role.getRoleCode());
		this.baseMapper.deleteRoleMenu(role.getRoleCode());
		this.baseMapper.deleteById(role.getRoleCode());
		return true;
	}
}
