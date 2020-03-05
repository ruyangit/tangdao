/**
 * 
 */
package com.tangdao.framework.context;

import org.springframework.beans.factory.InitializingBean;

import com.tangdao.framework.model.UserInfo;

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
	public UserInfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
