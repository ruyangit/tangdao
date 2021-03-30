/**
 *
 */
package com.tangdao.admin.web.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.admin.web.security.model.AuthUser;
import com.tangdao.admin.web.security.service.IUserDetailsService;
import com.tangdao.model.domain.User;
import com.tangdao.service.provider.UserService;

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
//			throw new BusinessException(CommonApiCode.COMMON_APPKEY_INVALID);
		}
		AuthUser authUser = new AuthUser();
		authUser.setId("1");
		authUser.setUsername("system");
		authUser.setUserType("1");
		authUser.setMgrType("1");
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		authUser.setPassword(bpe.encode("system"));
		return authUser;
	}

}
