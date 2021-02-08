/**
 *
 */
package com.tangdao.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.system.entity.Role;
import com.tangdao.system.mapper.RoleMapper;

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
	 * @param userCode
	 * @return
	 */
	public List<Role> findByUserCode(String userCode) {
		Role role = new Role();
		role.setUserCode(userCode);
		return baseMapper.findByUserCode(role);
	}

	/**
	 * 
	 * TODO 检查角色名称是否存在
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
	 * @param role
	 * @param menuCodes
	 */
	public void insertRoleMenu(Role role, String[] menuCodes) {
		// 删除原有角色的所有权限
		this.baseMapper.deleteRoleMenu(role.getRoleCode());
		// 新增角色授权
		if (menuCodes != null && menuCodes.length > 0 && StrUtil.isNotBlank(role.getRoleCode())) {
			this.baseMapper.insertRoleMenu(role.getRoleCode(), menuCodes);
		}
	}

	/**
	 * 
	 * TODO 刪除角色与用户数据
	 * @param roleCode
	 * @param userCode
	 * @return
	 */
	public int deleteRoleUser(String roleCode, String userCode) {
		// TODO Auto-generated method stub
		return this.baseMapper.deleteRoleUser(roleCode, userCode);
	}

	/**
	 * 
	 * TODO 保存角色与用户数据
	 * @param roleCode
	 * @param userCodes
	 * @return
	 */
	public int insertRoleUser(String roleCode, String[] userCodes) {
		int count = 0;
		if (userCodes != null && roleCode != null) {
			for (String userCode : userCodes) {
				this.baseMapper.deleteRoleUser(roleCode, userCode);
				count += this.baseMapper.insertRoleUser(roleCode, userCode);
			}
		}
		return count;
	}
}
