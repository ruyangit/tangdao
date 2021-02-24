/**
 *
 */
package com.tangdao.developer.service;

import org.springframework.stereotype.Service;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@Service
public class HostWhitelistService {

	/**
	 * 
	 * TODO 服务器IP是否允许通过
	 * @param userId
	 * @param ip
	 * @return
	 */
	public boolean ipAllowedPass(String userId, String ip) {
		
		return true;
	}
}
