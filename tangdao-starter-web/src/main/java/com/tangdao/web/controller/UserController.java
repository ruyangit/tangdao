/**
 *
 */
package com.tangdao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.ApiController;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.auth.ActionTypes;
import com.tangdao.core.auth.Secured;
import com.tangdao.model.User;
import com.tangdao.modules.user.service.UserService;

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

	@PostMapping
	public CommonResponse createUser(@RequestParam String username, @RequestParam String password,
			HttpServletResponse response, HttpServletRequest request) {
		User user = userService.findUserByUsername(username);
		if (user != null) {
			throw new IllegalArgumentException("user '" + username + "' already exist!");
		}
		userService.createUser(username, passwordEncoder.encode(password));
		return success("创建用户成功");
	}

	@Secured(resource = "user", action = ActionTypes.READ)
	@GetMapping
	public CommonResponse getUser(@RequestParam String username) {
		return success(userService.findUserByUsername(username));
	}
}
