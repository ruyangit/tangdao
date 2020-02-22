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
import com.tangdao.module.core.entity.User;
import com.tangdao.module.core.service.IUserService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController extends BaseController {

	@Autowired
	private IUserService userService;
	
	/**
	 * 用户表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/user")
	public IPage<User> page(IPage<User> page) {
		return userService.page(page);
	}

	/**
	 * 用户表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/users")
	public List<User> list() {
		return userService.list();
	}

	/**
	 * 用户表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/user/{id}")
	public User getUser(String id) {
		return userService.getById(id);
	}

	/**
	 * 用户表保存
	 * 
	 * @param user
	 * @return
	 */
	@PostMapping("/user")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated User user) {
		return userService.save(user);
	}

	/**
	 * 用户表修改
	 * 
	 * @param id
	 * @param user
	 * @return
	 */
	@PutMapping("/user")
	public boolean update(@RequestBody @Validated User user) {
		return userService.saveOrUpdate(user);
	}

	/**
	 * 用户表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/user/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return userService.removeById(id);
	}
}
