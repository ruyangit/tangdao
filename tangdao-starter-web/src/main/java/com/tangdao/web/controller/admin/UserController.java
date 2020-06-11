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
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.User;
import com.tangdao.model.dto.UserDTO;
import com.tangdao.modules.sys.service.UserService;
import com.tangdao.web.config.TangdaoProperties;

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
	
	@Autowired
	private TangdaoProperties properties;

	@GetMapping
	public CommonResponse page(Pageinfo page, UserDTO user) {
		user.setSuperAdmin(properties.getSuperAdmin());
		return success(userService.findMapsPage(page, user));
	}

	@Validate({ @Field(name = "user.username", rules = { @Rule(message = "账号不能为空") }),
			@Field(name = "user.password", rules = { @Rule(message = "密码不能为空") }) })
	@PostMapping
	public CommonResponse createUser(@RequestBody UserDTO user) {
		User eu = userService.findByUsername(user.getUsername());
		if (eu != null) {
			throw new IllegalArgumentException("用户 '" + eu.getUsername() + "' 已存在");
		}
		return success(userService.createUserAndRoleIds(user.getUsername(), passwordEncoder.encode(user.getPassword()), user.getRoleIds()));
	}

	@Validate({ @Field(name = "user.password", rules = { @Rule(message = "密码不能为空") }) })
	@PostMapping("/password/modify")
	public CommonResponse passwordModify(@RequestBody User user) {
		return success(userService.passwordModify(user.getId(), passwordEncoder.encode(user.getPassword())));
	}

	@PostMapping("/update")
	public CommonResponse updateUser(@RequestBody User user) {
		return success(userService.updateById(user));
	}

	@PostMapping("/delete")
	public CommonResponse deleteUser(@RequestBody User user) {
		return success(userService.deleteUser(user.getId()));
	}

}
