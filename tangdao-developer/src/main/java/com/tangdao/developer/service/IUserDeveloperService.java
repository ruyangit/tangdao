/**
 *
 */
package com.tangdao.developer.service;

import com.tangdao.developer.model.domain.UserDeveloper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public interface IUserDeveloperService {

	public UserDeveloper getByAppkey(String appKey);
}
