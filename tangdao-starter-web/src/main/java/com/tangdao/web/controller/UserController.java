/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.ApiController;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.auth.ActionTypes;
import com.tangdao.core.auth.Secured;
import com.tangdao.core.validate.Field;
import com.tangdao.core.validate.Rule;
import com.tangdao.core.validate.Validate;
import com.tangdao.model.User;
import com.tangdao.modules.user.service.UserService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@RestController
@RequestMapping(value = { "/v1/users" })
public class UserController extends ApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	@Secured(resource = "user", action = ActionTypes.READ)
	public CommonResponse page(String username, @RequestParam Integer current, @RequestParam Integer size) {
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		if (StrUtil.isNotBlank(username)) {
			queryWrapper.like("username", username);
		}
		return success(userService.page(new Page<User>(current, size), queryWrapper));
	}

	@Validate({ 
		@Field(name = "user.username", rules = { 
			@Rule(message = "账号不能为空")
		}),
		@Field(name = "user.password", rules = { 
			@Rule(message = "密码不能为空")
		}) 
	})
	@PostMapping
	public CommonResponse createUser(@RequestBody User user) {
		User eu = userService.findUserByUsername(user.getUsername());
		if (eu != null) {
			throw new IllegalArgumentException("用户 '" + eu.getUsername() + "' 已存在");
		}
		return success(userService.createUser(user.getUsername(), passwordEncoder.encode(user.getPassword())));
	}

	@PostMapping("/password/change")
	public CommonResponse passwordChange(@RequestBody User user) {
		return success("创建用户成功");
	}

	@PostMapping("/update")
	@Secured(resource = "user", action = ActionTypes.WRITE)
	public CommonResponse updateUser(@RequestBody User user) {
		return success(userService.updateById(user));
	}

	@PostMapping("/delete")
	@Secured(resource = "user", action = ActionTypes.WRITE)
	public CommonResponse deleteUser(@RequestBody User user) {
		return success(userService.removeById(user.getId()));
	}

}
