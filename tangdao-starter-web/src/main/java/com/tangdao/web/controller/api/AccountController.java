/**
 *
 */
package com.tangdao.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.CommonContext.UserSource;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.RuleType;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.User;
import com.tangdao.model.dto.RegisterDTO;
import com.tangdao.modules.sys.service.UserService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月12日
 */
@RestController
@RequestMapping(value = { "/api/account" })
public class AccountController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Validate({ @Field(name = "register.username", rules = { @Rule(message = "账号不能为空") }),
			@Field(name = "register.password", rules = { @Rule(message = "密码不能为空") }),
			@Field(name = "register.mobile", rules = { @Rule(type = RuleType.MOBILE) }) })
	@PostMapping("/register")
	public CommonResponse register(@RequestBody RegisterDTO register) {
		User eu = userService.findByUsername(register.getUsername());
		if (eu != null) {
			throw new IllegalArgumentException("账号 '" + eu.getUsername() + "' 已存在");
		}
		User user = new User();
		user.setUsername(register.getUsername());
		user.setPassword(passwordEncoder.encode(register.getPassword()));
		user.setMobile(register.getMobile());
		user.setCreateSource(UserSource.WEB.getName());
		userService.save(user);
		return success(userService.save(user));
	}
}
