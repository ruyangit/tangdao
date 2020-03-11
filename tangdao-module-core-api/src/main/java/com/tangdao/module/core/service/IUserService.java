package com.tangdao.module.core.service;

import com.tangdao.framework.service.ICrudService;
import com.tangdao.module.core.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
public interface IUserService extends ICrudService<User> {

	/**
	 * todo 创建用户
	 * @param user
	 * @return
	 */
	public boolean createUser(User user);
}
