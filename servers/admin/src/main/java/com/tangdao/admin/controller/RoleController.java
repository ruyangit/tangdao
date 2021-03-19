/**
 *
 */
package com.tangdao.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.model.domain.Role;
import com.tangdao.core.service.RoleService;
import com.tangdao.core.web.BaseController;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@GetMapping("record")
	public CommonResponse record(Page<Role> page, Role role) {
		LambdaQueryWrapper<Role> queryWrapper = Wrappers.<Role>lambdaQuery();
		if (StrUtil.isNotBlank(role.getRoleCode())) {
			queryWrapper.likeRight(Role::getRoleCode, role.getRoleCode());
		}
		if (StrUtil.isNotBlank(role.getRoleName())) {
			queryWrapper.likeRight(Role::getRoleName, role.getRoleName());
		}
		if (StrUtil.isNotBlank(role.getStatus())) {
			queryWrapper.eq(Role::getStatus, role.getStatus());
		}
		return success(roleService.page(page, queryWrapper));
	}
}
