/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.tangdao.core.service.BaseService;
import com.tangdao.service.mapper.UserMapper;
import com.tangdao.service.model.domain.User;
import com.tangdao.service.model.dto.UserDTO;
import com.tangdao.service.model.vo.UserRole;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 用户服务
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
@Service
public class UserService extends BaseService<UserMapper, User> {
	
	/**
	 * 
	 * TODO 根据用户名查询
	 * 
	 * @param username
	 * @return
	 */
	public User getByUsername(String username) {
		return super.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
	}

	/**
	 * 
	 * TODO 检查用户名是否存在
	 * 
	 * @param oldUsername
	 * @param username    存在-true，不存在-false
	 * @return
	 */
	public boolean checkUsernameExists(String oldUsername, String username) {
		if (username != null && username.equals(oldUsername)) {
			return true;
		} else if (username != null && super.count(Wrappers.<User>lambdaQuery().eq(User::getUsername, username)) == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * TODO 删除用户角色
	 * 
	 * @param userRole
	 * @return
	 */
	public boolean deleteUserRole(UserRole userRole) {
		return SqlHelper.retBool(super.getBaseMapper().deleteUserRole(userRole));
	}

	/**
	 * 
	 * TODO 保存用户角色
	 * 
	 * @param userRole
	 * @return
	 */
	public boolean insertUserRole(UserRole userRole) {
		return SqlHelper.retBool(super.getBaseMapper().insertUserRole(userRole));
	}

	/**
	 * 
	 * TODO 创建用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean createUser(UserDTO user) {
		super.save(user);
		// 保存角色
		if(CollUtil.isNotEmpty(user.getRoleIds())) {
			UserRole userRole = new UserRole();
			userRole.setUserId(user.getId());
			userRole.setRoleId(user.getRoleId());
			userRole.setRoleIds(user.getRoleIds());
			this.insertUserRole(userRole);
		}
		return true;
	}

	/**
	 * 
	 * TODO 删除用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(UserDTO user) {
		super.removeById(user.getId());
		UserRole userRole = new UserRole();
		userRole.setUserId(user.getId());
		this.deleteUserRole(userRole);
		return true;
	}

}
