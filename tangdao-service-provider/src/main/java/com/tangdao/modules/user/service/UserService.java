/**
 *
 */
package com.tangdao.modules.user.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.User;
import com.tangdao.modules.user.mapper.UserMapper;

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
		user.setCreated(new Date());
		return this.save(user);
	}

	public Pageinfo findMapsPage(Pageinfo page, User user) {
		return getBaseMapper().findMapsPage(page, user);
	}
}
