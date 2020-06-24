/**
 *
 */
package com.tangdao.web.security.user;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import cn.hutool.core.collection.CollUtil;
import lombok.Getter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Getter
public class SecurityUserDetails implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SecurityUser securityUser;

	public SecurityUserDetails(SecurityUser securityUser) {
		this.securityUser = securityUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return CollUtil.newArrayList();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return securityUser.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return securityUser.getUsername();
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
		// TODO Auto-generated method stub
		return true;
	}

}
