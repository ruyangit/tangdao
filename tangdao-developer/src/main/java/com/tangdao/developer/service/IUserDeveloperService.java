/**
 *
 */
package com.tangdao.developer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tangdao.developer.model.domain.UserDeveloper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public interface IUserDeveloperService extends IService<UserDeveloper> {

	public UserDeveloper getByAppkey(String appKey);
}
