package com.tangdao.module.core.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.framework.web.BaseController;
import com.tangdao.module.core.model.domain.Role;
import com.tangdao.module.core.service.IRoleService;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@RestController
@RequestMapping(value = "/{env}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
	/**
	 * 角色分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping
	public IPage<Role> page(Page<Role> page) {
		return roleService.page(page);
	}

	/**
	 * 角色信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{roleId}")
	public Role get(@PathVariable("roleId") String roleId) {
		return roleService.getById(roleId);
	}

	/**
	 * 角色保存
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Role role) {
		return roleService.save(role);
	}

	/**
	 * 角色修改
	 * 
	 * @param id
	 * @param role
	 * @return
	 */
	@PutMapping("/{roleId}")
	public boolean update(@PathVariable("roleId") String roleId, @RequestBody @Validated Role role) {
		return roleService.saveOrUpdate(role);
	}

	/**
	 * 角色删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{roleId}")
	public boolean delete(@PathVariable("roleId") String roleId) {
		return roleService.removeById(roleId);
	}
}
