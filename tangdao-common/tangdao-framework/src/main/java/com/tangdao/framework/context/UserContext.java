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
	 * todo 获取用户信息
	 * @return
	 */
	public UserInfo getUserInfo();
	
	/**
	 * todo 密码加密
	 * @param rawPassword
	 * @return
	 */
	public String passwordEncode(String rawPassword);
	
	/**
	 * todo 密码校验
	 * @param rawPassword 原密码
	 * @param encodedPassword 加密密码
	 * @return
	 */
	public boolean passwordMatches(String rawPassword, String encodedPassword);
}
