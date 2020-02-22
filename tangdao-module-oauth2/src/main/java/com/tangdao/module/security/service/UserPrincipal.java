/**
 * 
 */
package com.tangdao.module.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tangdao.module.core.model.domain.User;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
public class UserPrincipal implements UserDetails, CredentialsContainer, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String password;
	
	private String tenantId;

	private List<String> authorities;
	
	/**
	 * 
	 * @param user
	 */
	public UserPrincipal(User user) {
		this.setUsername(user.getLoginName());
		this.setPassword(user.getPassword());
		this.setTenantId(user.getTenantId());
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (authorities != null) {
			return authorities.stream().map(r -> new SimpleGrantedAuthority(r)).collect(Collectors.toList());
		}
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
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the tenantId
	 */
	public String getTenantId() {
		return tenantId;
	}

	/**
	 * @param tenantId the tenantId to set
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * @param authorities the authorities to set
	 */
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
	
}
