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
import com.tangdao.module.core.entity.Group;
import com.tangdao.module.core.service.IGroupService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 用户组 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController extends BaseController {

	@Autowired
	private IGroupService groupService;
	
	/**
	 * 用户组分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/group")
	public IPage<Group> page(IPage<Group> page) {
		return groupService.page(page);
	}

	/**
	 * 用户组列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/groups")
	public List<Group> list() {
		return groupService.list();
	}

	/**
	 * 用户组信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/group/{id}")
	public Group get(String id) {
		return groupService.getById(id);
	}

	/**
	 * 用户组保存
	 * 
	 * @param group
	 * @return
	 */
	@PostMapping("/group")
	@ResponseStatus(HttpStatus.CREATED)
	public Result<Object> post(@RequestBody @Validated Group group) {
		groupService.save(group);
		return Result.render(OpenApiCode.TRUE, "新增成功");
	}

	/**
	 * 用户组修改
	 * 
	 * @param id
	 * @param group
	 * @return
	 */
	@PutMapping("/group")
	public Result<Object> put(@RequestBody @Validated Group group) {
		groupService.saveOrUpdate(group);
		return Result.render(OpenApiCode.TRUE, "修改成功");
	}

	/**
	 * 用户组删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/group/{id}")
	public Result<Object> delete(@PathVariable("id") String id) {
		groupService.removeById(id);
		return Result.render(OpenApiCode.TRUE, "删除成功");
	}
}
