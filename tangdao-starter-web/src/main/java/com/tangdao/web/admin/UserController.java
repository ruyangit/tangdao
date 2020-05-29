/**
 *
 */
package com.tangdao.web.admin;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.ApiController;
import com.tangdao.common.CommonResponse;
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
@RequestMapping("/api/user")
public class UserController extends ApiController {

	@Resource
	private UserService userService;

	@GetMapping("/findByUsername")
	public CommonResponse findByUsername(String username) {
		return success(userService.findUserByUsername(username));
	}
}
