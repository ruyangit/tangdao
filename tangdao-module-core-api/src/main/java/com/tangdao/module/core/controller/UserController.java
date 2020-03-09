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
import com.tangdao.framework.annotation.Authorize;
import com.tangdao.framework.web.BaseController;
import com.tangdao.module.core.entity.User;
import com.tangdao.module.core.service.IUserService;

/**
 * <p>
 * 用户资源
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@RestController
@RequestMapping(value = "/{env}/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	
	/**
	 * 用户分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping
	@Authorize("sys:user:PageUser")
	public IPage<User> page(Page<User> page) {
		return userService.page(page);
	}

	/**
	 * 用户保存
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Authorize("sys:user:CreateUser")
	public boolean create(@RequestBody @Validated User user) {
		return userService.save(user);
	}
	
	/**
	 * 用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@GetMapping("/{userId}")
	@Authorize("sys:user:GetUser")
	public User get(@PathVariable("userId") String userId) {
		return userService.getById(userId);
	}

	/**
	 * 用户修改
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@PutMapping("/{userId}")
	@Authorize("sys:user:UpdateUser")
	public boolean update(@PathVariable("userId") String userId, @RequestBody @Validated User user) {
		return userService.updateById(user);
	}

	/**
	 * 用户删除
	 * 
	 * @param userId
	 * @return
	 */
	@DeleteMapping("/{userId}")
	@Authorize("sys:user:DeleteUser")
	public boolean delete(@PathVariable("userId") String userId) {
		return userService.removeById(userId);
	}
}
