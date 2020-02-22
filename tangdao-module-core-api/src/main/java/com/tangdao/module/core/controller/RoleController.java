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
import com.tangdao.framework.constant.OpenApiCode;
import com.tangdao.framework.protocol.Result;
import com.tangdao.module.core.entity.Role;
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
	public Role get(String id) {
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
	public Result<Object> post(@RequestBody @Validated Role role) {
		roleService.save(role);
		return Result.render(OpenApiCode.TRUE, "新增成功");
	}

	/**
	 * 角色表修改
	 * 
	 * @param id
	 * @param role
	 * @return
	 */
	@PutMapping("/role")
	public Result<Object> put(@RequestBody @Validated Role role) {
		roleService.saveOrUpdate(role);
		return Result.render(OpenApiCode.TRUE, "修改成功");
	}

	/**
	 * 角色表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/role/{id}")
	public Result<Object> delete(@PathVariable("id") String id) {
		roleService.removeById(id);
		return Result.render(OpenApiCode.TRUE, "删除成功");
	}
}
