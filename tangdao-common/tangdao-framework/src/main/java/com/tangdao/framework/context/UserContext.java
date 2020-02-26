/**
 * 
 */
package com.tangdao.framework.context;

import java.util.List;

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
public interface UserContext {
	
	/**
	 * 获取用户信息
	 * @return
	 */
	public UserInfo getPrincipal();

	/**
	 * 获取授权策略
	 * @return
	 */
	public List<Policy> listPolicies();
}
