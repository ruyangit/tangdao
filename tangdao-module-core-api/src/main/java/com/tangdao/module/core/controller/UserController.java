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
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	
	/**
	 * 用户分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/users")
	@Authorize("core:user:ListUsers")
	public IPage<User> lists(IPage<User> page) {
		return userService.page(page);
	}

	/**
	 * 用户列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/user")
	@Authorize("core:user:ListUser")
	public List<User> list() {
		return userService.list();
	}

	/**
	 * 用户保存
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	@Authorize("core:user:CreateUser")
	public boolean save(@RequestBody @Validated User user) {
		return userService.save(user);
	}
	
	/**
	 * 用户信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}")
	@Authorize("core:user:GetUser")
	public User getUser(@PathVariable("id") String id) {
		return userService.getById(id);
	}

	/**
	 * 用户修改
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@PutMapping("/user/{id}")
	@Authorize("core:user:UpdateUser")
	public boolean update(@PathVariable("id") String id, @RequestBody @Validated User user) {
		return userService.updateById(user);
	}

	/**
	 * 用户删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/user/{id}")
	@Authorize("core:user:DeleteUser")
	public boolean delete(@PathVariable("id") String id) {
		return userService.removeById(id);
	}
}
