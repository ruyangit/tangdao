/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.Role;
import com.tangdao.modules.sys.mapper.RoleMapper;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月2日
 */
@Service
public class RoleService extends BaseService<RoleMapper, Role> {

	public Role findByRoleName(String roleName) {
		return findRole(this.list(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, roleName)));
	}

	public Role findRole(List<Role> roles) {
		if (CollUtil.isNotEmpty(roles)) {
			return roles.get(0);
		}
		return null;
	}
}
