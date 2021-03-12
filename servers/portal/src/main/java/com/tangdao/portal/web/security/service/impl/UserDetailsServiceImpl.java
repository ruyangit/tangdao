/**
 *
 */
package com.tangdao.portal.web.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.service.UserService;
import com.tangdao.portal.web.security.model.AuthUser;
import com.tangdao.portal.web.security.service.IUserDetailsService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@Service
public class UserDetailsServiceImpl implements IUserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
		if (user == null) {
			throw new BusinessException(CommonApiCode.COMMON_APPKEY_INVALID);
		}
		AuthUser authUser = new AuthUser();
		authUser.setId(user.getId());
		authUser.setUsername(user.getUsername());
		return authUser;
	}

}
