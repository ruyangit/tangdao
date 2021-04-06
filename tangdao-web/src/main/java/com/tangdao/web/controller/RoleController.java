/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.Role;
import com.tangdao.service.model.dto.RoleDTO;
import com.tangdao.service.provider.RoleService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 角色接口
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

	@LogOpt(logTitle = "获取列表分页数据")
	@GetMapping("/page")
	public CommonResponse page(Page<Role> page, Role role) {
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
		return renderResult(roleService.page(page, queryWrapper));
	}

	@LogOpt(logTitle = "获取列表数据")
	@GetMapping("/list")
	public CommonResponse list(Role role) {
		LambdaQueryWrapper<Role> queryWrapper = Wrappers.<Role>lambdaQuery();
		return renderResult(roleService.list(queryWrapper));
	}

	@LogOpt(logTitle = "新增或更新数据")
	@PostMapping("/save")
	public CommonResponse save(@RequestBody RoleDTO role) {
		if (roleService.checkRoleCodeExists(role.getOldRoleCode(), role.getRoleCode())) {
			return renderResult(Global.FALSE, "保存角色'" + role.getRoleCode() + "'失败，角色编码已存在");
		}
		roleService.saveOrUpdate(role);
		return renderResult(Global.TRUE, "保存成功");
	}
	
	@LogOpt(logTitle = "根据主键删除数据")
	@PostMapping("/delete")
	public CommonResponse delete(@RequestBody RoleDTO role) {
		roleService.removeById(role.getId());
		return renderResult(Global.TRUE, "删除成功");
	}
}
