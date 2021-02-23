/**
 *
 */
package com.tangdao.developer.service;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public interface IHostWhitelistService {

	public boolean ipAllowedPass(String userCode, String ip);
}
