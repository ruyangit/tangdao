/**
 *
 */
package com.tangdao.developer.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.model.domain.paas.UserDeveloper;
import com.tangdao.core.service.BaseService;
import com.tangdao.developer.dao.UserDeveloperMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@Service
public class UserDeveloperService extends BaseService<UserDeveloperMapper, UserDeveloper> {

	public UserDeveloper getByAppKey(String appKey) {
		QueryWrapper<UserDeveloper> queryWrapper = new QueryWrapper<UserDeveloper>();
		queryWrapper.eq("app_key", appKey);
		queryWrapper.eq("status", UserDeveloper.STATUS_NORMAL);
		return this.getBaseMapper().selectOne(queryWrapper);
	}
}
