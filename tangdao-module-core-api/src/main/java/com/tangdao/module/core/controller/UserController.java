package com.tangdao.module.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.module.core.model.domain.User;
import com.tangdao.module.core.service.IUserService;

@RestController
@RequestMapping(value = "/api/{version}/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
	
	@Autowired
	private IUserService userService;

	@GetMapping
	public IPage<User> page(Page<User> page, User user) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		return userService.page(page, queryWrapper);
	}
	
	@GetMapping
	public List<User> list(User user) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		return userService.list(queryWrapper);
	}
	
	@GetMapping("{id}")
	public User get(@PathVariable("id") String id) {
		return userService.getById(id);
	}
}
