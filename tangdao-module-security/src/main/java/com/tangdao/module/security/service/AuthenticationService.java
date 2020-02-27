/**
 * 
 */
package com.tangdao.module.security.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tangdao.common.servlet.ServletUtils;
import com.tangdao.framework.context.UserContextAdapter;
import com.tangdao.framework.model.Policy;
import com.tangdao.framework.model.Tenant;
import com.tangdao.framework.model.UserInfo;
import com.tangdao.module.core.mapper.UserMapper;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Service
public class AuthenticationService extends UserContextAdapter implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 用户服务
	 */
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		String tenantId = ServletUtils.getParameter("tenantId");
		if (StrUtil.isEmpty(tenantId)) {
			tenantId = Tenant.DEFAULT_VALUE;
		}
		try {
			UserInfo user = new UserInfo();
			user.setUsername(username);
			user.setTenantId(tenantId);
			List<UserInfo> users = userMapper.listUserInfo(user);
			if (null == users) {
				throw new UsernameNotFoundException("User with username " + username + " not founded");
			}
			return new UserPrincipal(
					users.stream().collect(Collectors.toMap(UserInfo::getTenantId, Function.identity())).get(tenantId));
		} catch (UsernameNotFoundException e) {
			logger.error("Error Username not found method loadUserByUsername in class AuthenticationService: ", e);
		} catch (Exception e) {
			logger.error("Error method loadUserByUsername in class AuthenticationService: ", e);
		}
		return null;
	}

	@Override
	public UserInfo getPrincipal() {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof UserPrincipal) {
			return ((UserPrincipal) principal).getUser();
		}
		return null;
	}

	@Override
	public List<Policy> listPolicies() {
		// TODO Auto-generated method stub
		return super.listPolicies();
	}

}
