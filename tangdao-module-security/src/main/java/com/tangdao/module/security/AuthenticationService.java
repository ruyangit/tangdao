/**
 * 
 */
package com.tangdao.module.security;

import java.util.ArrayList;
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
import com.tangdao.framework.exception.ServiceException;
import com.tangdao.framework.model.Tenant;
import com.tangdao.framework.model.UserInfo;
import com.tangdao.module.core.mapper.UserMapper;
import com.tangdao.module.security.model.UserPrincipal;

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
	public UserDetails loadUserByUsername(String loginName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		String tenantId = ServletUtils.getParameter("tenantId");
		if (StrUtil.isEmpty(tenantId)) {
			tenantId = Tenant.DEFAULT_VALUE;
		}
		try {
			UserInfo user = new UserInfo();
			user.setLoginName(loginName);
			user.setTenantId(tenantId);
			List<UserInfo> users = userMapper.listUserInfo(user);
			user = users.stream().collect(Collectors.toMap(UserInfo::getTenantId, Function.identity())).get(tenantId);
			if (null == user) {
				throw new UsernameNotFoundException("User with loginName " + loginName + " not founded");
			}
			List<String> roles = new ArrayList<String>();
			roles.add("admin");
			user.setRoles(roles);
			return new UserPrincipal(user);
		} catch (UsernameNotFoundException e) {
			logger.error("Error Username not found method loadUserByUsername in class AuthenticationService: ", e);
			throw new UsernameNotFoundException("用户登录账号不存在");
		} catch (Exception e) {
			throw new ServiceException("Error method loadUserByUsername in class AuthenticationService: ", e);
		}
	}

	@Override
	public UserInfo getUserInfo() {
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

}
