/**
 * 
 */
package com.tangdao.framework.context;

import com.tangdao.framework.model.UserInfo;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月26日
 */
public interface UserContext {
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public UserInfo getPrincipal();
}
