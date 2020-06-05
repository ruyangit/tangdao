/**
 *
 */
package com.tangdao.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.validate.Field;
import com.tangdao.core.validate.Rule;
import com.tangdao.core.validate.Validate;
import com.tangdao.core.web.BaseController;
import com.tangdao.model.domain.User;
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
@RequestMapping(value = { "/admin/users" })
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping
	public CommonResponse page(Pageinfo page, User user) {
		return success(userService.findMapsPage(page, user));
	}

	@Validate({ @Field(name = "user.username", rules = { @Rule(message = "账号不能为空") }),
			@Field(name = "user.password", rules = { @Rule(message = "密码不能为空") }) })
	@PostMapping
	public CommonResponse createUser(@RequestBody User user) {
		User eu = userService.findByUsername(user.getUsername());
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
	public CommonResponse updateUser(@RequestBody User user) {
		return success(userService.updateById(user));
	}

	@PostMapping("/delete")
	public CommonResponse deleteUser(@RequestBody User user) {
		return success(userService.removeById(user.getId()));
	}

}
