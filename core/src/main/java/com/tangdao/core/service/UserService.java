/**
 *
 */
package com.tangdao.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.dao.UserMapper;
import com.tangdao.core.model.domain.User;

/**
 * <p>
 * TODO 用户服务
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
public class UserService extends BaseService<UserMapper, User> {

	/**
	 * 
	 * TODO 根据用户名查询
	 * @param username
	 * @return
	 */
	public User getByUsername(String username) {
		return super.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
	}

	/**
	 * 
	 * TODO 检查用户名是否存在
	 * @param oldUsername
	 * @param username
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
	
}
