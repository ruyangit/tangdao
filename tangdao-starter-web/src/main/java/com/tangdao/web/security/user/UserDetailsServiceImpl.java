/**
 *
 */
package com.tangdao.web.security.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tangdao.model.domain.User;
import com.tangdao.modules.sys.service.UserService;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月29日
 */
@Service
public class UserDetailsServiceImpl implements IUserDetailsService{

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userService.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		SecurityUser securityUser = new SecurityUser();
		BeanUtil.copyProperties(user, securityUser);
		return new SecurityUserDetails(securityUser);
	}
}
