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
import com.tangdao.module.core.entity.Group;
import com.tangdao.module.core.service.IGroupService;

/**
 * <p>
 * 用户组 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-03-11
 */
@RestController
@RequestMapping(value = "/{env}/groups", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController extends BaseController {

	@Autowired
	private IGroupService groupService;
	
	/**
	 * 用户组分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping
	public IPage<Group> lists(Page<Group> page) {
		return groupService.page(page);
	}

	/**
	 * 用户组信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{groupId}")
	public Group getGroup(String groupId) {
		return groupService.getById(groupId);
	}

	/**
	 * 用户组保存
	 * 
	 * @param group
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Group group) {
		return groupService.save(group);
	}

	/**
	 * 用户组修改
	 * 
	 * @param id
	 * @param group
	 * @return
	 */
	@PutMapping("/{groupId}")
	public boolean update(@PathVariable("groupId") String groupId, @RequestBody @Validated Group group) {
		return groupService.saveOrUpdate(group);
	}

	/**
	 * 用户组删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{groupId}")
	public boolean delete(@PathVariable("groupId") String groupId) {
		return groupService.removeById(groupId);
	}
}
