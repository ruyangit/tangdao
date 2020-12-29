/**
 *
 */
package com.tangdao.web.controller.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.web.BaseController;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月12日
 */
@RestController
@RequestMapping(value = { "/api" })
public class AccountController extends BaseController {

//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	@Validate({ @Field(name = "register.username", rules = { @Rule(message = "账号不能为空") }),
//			@Field(name = "register.password", rules = { @Rule(message = "密码不能为空") }),
//			@Field(name = "register.mobile", rules = { @Rule(type = RuleType.MOBILE) }) })
//	@PostMapping("/account-register")
//	public CommonResponse register(@RequestBody RegisterDTO register) {
//		User eu = userService.findByUsername(register.getUsername());
//		if (eu != null) {
//			throw new IllegalArgumentException("账号 '" + eu.getUsername() + "' 已存在");
//		}
//		User user = new User();
//		user.setUsername(register.getUsername());
//		user.setPassword(passwordEncoder.encode(register.getPassword()));
//		user.setMobile(register.getMobile());
//		user.setCreateSource(UserSource.WEB.getName());
//		userService.save(user);
//		return success(userService.save(user));
//	}
}
