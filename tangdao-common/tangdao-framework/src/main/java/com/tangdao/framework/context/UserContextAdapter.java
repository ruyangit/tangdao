/**
 * 
 */
package com.tangdao.framework.context;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;

import com.tangdao.framework.entity.Policy;
import com.tangdao.framework.entity.UserInfo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public abstract class UserContextAdapter implements UserContext, InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public UserInfo getPrincipal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Policy> listPolicies() {
		// TODO Auto-generated method stub
		return null;
	}

}
