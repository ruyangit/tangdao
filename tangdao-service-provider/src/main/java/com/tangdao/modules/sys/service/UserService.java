/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.User;
import com.tangdao.model.domain.UserRole;
import com.tangdao.model.dto.UserDTO;
import com.tangdao.modules.sys.mapper.UserMapper;
import com.tangdao.modules.sys.mapper.UserRoleMapper;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@Service
public class UserService extends BaseService<UserMapper, User> {
	
	@Autowired
	private UserRoleMapper userRoleMapper;

	public User findByUsername(String username) {
		return findUser(this.list(Wrappers.<User>lambdaQuery().eq(User::getUsername, username)));
	}

	public User findByMobile(String mobile) {
		return findUser(this.list(Wrappers.<User>lambdaQuery().eq(User::getMobile, mobile)));
	}

	public User findById(String id) {
		return findUser(this.list(Wrappers.<User>lambdaQuery().eq(User::getId, id)));
	}

	public User findUser(List<User> users) {
		if (CollUtil.isNotEmpty(users)) {
			return users.get(0);
		}
		return null;
	}
	
	public boolean createUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return this.save(user);
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean createUserAndRoleIds(String username, String password, List<String> roleIds) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		this.save(user);
		this.userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, user.getId()));
		roleIds.forEach(roleId ->{
			UserRole ur = new UserRole();
			ur.setRoleId(roleId);
			ur.setUserId(user.getId());
			this.userRoleMapper.insert(ur);
		});
		return true;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteUser(String id) {
		this.userRoleMapper.delete(Wrappers.<UserRole>lambdaQuery().eq(UserRole::getUserId, id));
		return super.removeById(id);
	}
	
	public boolean passwordModify(String id, String password) {
		User user = new User();
		user.setId(id);
		user.setPassword(password);
		user.setModified(new Date());
		return this.updateById(user);
	}
	
	public boolean lastLoginUserModify(String id, String lastLoginIp) {
		User user = new User();
		user.setId(id);
		user.setLastLoginIp(lastLoginIp);
		user.setLastLoginDate(new Date());
		return this.updateById(user);
	}

	public Pageinfo findMapsPage(Pageinfo page, UserDTO user) {
		return getBaseMapper().findMapsPage(page, user);
	}
	
	public List<Map<String, Object>> findUserRoleMapsList(UserDTO user){
		return userRoleMapper.findUserRoleMapsList(user);
	}
}
