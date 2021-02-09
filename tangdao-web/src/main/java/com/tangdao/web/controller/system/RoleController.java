/**
 *
 */
package com.tangdao.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.system.model.domain.Role;
import com.tangdao.system.service.RoleService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@RestController
@RequestMapping("v1/system")
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

//	@Autowired
//	private MenuService menuService;

	@GetMapping("/queryRolePage")
	public CommonResponse findRolePage(Page<Role> page, Role role) {
		QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
		if (StrUtil.isNotBlank(role.getRoleName())) {
			queryWrapper.like("role_name", role.getRoleName());
		}
		return success(roleService.page(page, queryWrapper));
	}

	@GetMapping("/queryRoleList")
	public CommonResponse queryRoleList(Role role) {
		QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
		if (StrUtil.isNotBlank(role.getRoleName())) {
			queryWrapper.like("role_name", role.getRoleName());
		}
		queryWrapper.eq("status", Role.NORMAL);
		return success(roleService.list(queryWrapper));
	}
}
