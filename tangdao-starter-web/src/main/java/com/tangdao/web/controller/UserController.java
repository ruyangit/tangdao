/**
 *
 */
package com.tangdao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.common.ApiController;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.auth.AccessException;
import com.tangdao.model.User;
import com.tangdao.modules.user.service.UserService;
import com.tangdao.web.security.AuthConfig;
import com.tangdao.web.security.AuthManagerImpl;
import com.tangdao.web.security.user.SecurityUser;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@RestController
@RequestMapping(value = { "/v1/auth", "/v1/auth/users" })
public class UserController extends ApiController {

	@Autowired
	private AuthManagerImpl authManager;

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public CommonResponse login(@RequestParam String username, @RequestParam String password,
			HttpServletResponse response, HttpServletRequest request) throws AccessException {
		SecurityUser user = (SecurityUser) authManager.login(request);

		response.addHeader(AuthConfig.AUTHORIZATION_HEADER, AuthConfig.TOKEN_PREFIX + user.getToken());

		JSONObject result = new JSONObject();
		result.put("access_token", user.getToken());
		result.put("username", username);
		return success(result);
	}

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
}
