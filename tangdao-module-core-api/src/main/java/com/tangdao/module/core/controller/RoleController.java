package com.tangdao.module.core.controller;


import java.util.List;

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
import com.tangdao.module.core.model.domain.Role;
import com.tangdao.module.core.service.IRoleService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
	/**
	 * 角色表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/role")
	public IPage<Role> page(IPage<Role> page) {
		return roleService.page(page);
	}

	/**
	 * 角色表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/roles")
	public List<Role> list() {
		return roleService.list();
	}

	/**
	 * 角色表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/role/{id}")
	public Role getRole(String id) {
		return roleService.getById(id);
	}

	/**
	 * 角色表保存
	 * 
	 * @param role
	 * @return
	 */
	@PostMapping("/role")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Role role) {
		return roleService.save(role);
	}

	/**
	 * 角色表修改
	 * 
	 * @param id
	 * @param role
	 * @return
	 */
	@PutMapping("/role")
	public boolean update(@RequestBody @Validated Role role) {
		return roleService.saveOrUpdate(role);
	}

	/**
	 * 角色表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/role/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return roleService.removeById(id);
	}
}
