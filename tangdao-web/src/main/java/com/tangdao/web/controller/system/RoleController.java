/**
 *
 */
package com.tangdao.web.controller.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.system.model.domain.Menu;
import com.tangdao.system.model.domain.Role;
import com.tangdao.system.service.MenuService;
import com.tangdao.system.service.RoleService;

import cn.hutool.core.collection.CollUtil;
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
	
	@Autowired
	private MenuService menuService;
	
	@GetMapping("/getRole")
	public CommonResponse getRole(String roleCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("role", roleService.getById(roleCode));
		List<Menu> menuList = menuService.findByRoleCode(roleCode);
		data.put("menuCodes", CollUtil.getFieldValues(menuList, "menuCode"));
		return success(data);
	}

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
		queryWrapper.eq("status", Role.STATUS_NORMAL);
		return success(roleService.list(queryWrapper));
	}

	@PostMapping("/saveOrUpdateRole")
	public CommonResponse saveRole(@RequestBody Role role) {
		if (!roleService.checkRoleNameExists(role.getOldRoleName(), role.getRoleName())) {
			throw new IllegalArgumentException("角色 '" + role.getRoleName() + "' 已存在");
		}
		if (StrUtil.isNotBlank(role.getRoleCode())) {
			roleService.insertRoleMenu(role);
		}
		return success(roleService.saveOrUpdate(role));
	}
	
	@PostMapping("/deleteRole")
	public CommonResponse deleteRole(@RequestBody Role role) {
		return success(roleService.deleteRole(role));
	}

}
