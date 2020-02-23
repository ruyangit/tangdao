package com.tangdao.module.core.service;

import com.tangdao.framework.model.UserVo;
import com.tangdao.framework.service.ICrudService;
import com.tangdao.module.core.model.domain.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
public interface IUserService extends ICrudService<User> {

	/**
	 * 根据用户账号获取用户信息
	 * @param loginName
	 * @param tenantId
	 * @return
	 */
	public UserVo getUserVo(String loginName, String tenantId);
}
