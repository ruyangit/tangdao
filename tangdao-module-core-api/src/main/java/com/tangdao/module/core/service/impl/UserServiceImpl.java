package com.tangdao.module.core.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.framework.model.UserVo;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import com.tangdao.module.core.mapper.UserMapper;
import com.tangdao.module.core.model.domain.User;
import com.tangdao.module.core.service.IUserService;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, User> implements IUserService {
	
	@Override
	public UserVo getUserVo(String loginName, String tenantId) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		queryWrapper.eq("login_name", loginName);
//		queryWrapper.eq("tenant_id", tenantId);
		User user = this.getOne(queryWrapper);
		if (null == user) {
			return null;
		}
		UserVo target = new UserVo();
		BeanUtil.copyProperties(user, target);
		return target;
	}

}
