/**
 * 
 */
package com.tangdao.module.security.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tangdao.framework.model.UserVo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
public class UserPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 用户信息
	 */
	private UserVo userVo;
	
	/**
	 * 
	 * @param userVo
	 */
	public UserPrincipal(UserVo userVo) {
		this.userVo = userVo;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
//		Set<SimpleGrantedAuthority> collect = userVo.getRoles().stream()
//				.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getPermission().toUpperCase()))
//				.collect(Collectors.toSet());
//		
//		if (userVo.getAuthorities() != null) {
//			collect.addAll(userVo.getAuthorities().stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList()));
//		}
		return new ArrayList<GrantedAuthority>();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userVo.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userVo.getLoginName();
	}

	/**
	 * @return the userVo
	 */
	public UserVo getUserVo() {
		return userVo;
	}
	
}
