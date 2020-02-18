package com.tangdao.module.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.module.core.model.domain.User;
import com.tangdao.module.core.service.IUserService;

@RestController
@RequestMapping(value = "users", produces="application/json;charset=utf-8")
public class UserController {
	
	@Autowired
	private IUserService userService;

	@GetMapping("{user_code}")
	public User get(@PathVariable("user_code") String userCode) {
		return userService.getById(userCode);
	}
}
