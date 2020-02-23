/**
 * 
 */
package com.tangdao.module.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.module.core.model.domain.User;
import com.tangdao.module.core.service.IUserService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Service
public class AuthenticationService implements UserDetailsService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getLoginName, username));
			if(null == user) {
				throw new UsernameNotFoundException("User with username " + username + " not founded");
			}
			return new UserPrincipal(user);
		} catch (UsernameNotFoundException e) {
			log.error("Error Username not found method loadUserByUsername in class AuthenticationService: ", e);
		} catch (Exception e) {
			log.error("Error method loadUserByUsername in class AuthenticationService: ", e);
		}
		return null;
	}

}
