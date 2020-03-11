package com.tangdao.module.core.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tangdao.framework.context.UserContext;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import com.tangdao.module.core.entity.User;
import com.tangdao.module.core.mapper.UserMapper;
import com.tangdao.module.core.service.IUserService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, User> implements IUserService {

	@Autowired
	private UserContext userContext;
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public boolean createUser(User user) {
		logger.info("create user success...");
		user.setNickname(StrUtil.isEmpty(user.getNickname())?user.getLoginName():user.getNickname());
		if(StrUtil.isNotBlank(user.getPassword())) {
			user.setPassword(userContext.passwordEncode(user.getPassword()));
		}
		return this.save(user);
	}

}
