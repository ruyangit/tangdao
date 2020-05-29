/**
 *
 */
package com.tangdao.web.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(value = { "/v1/auth", "/v1/user" })
public class UserController extends ApiController {

	@Resource
	private UserService userService;

	@PostMapping("/login")
	public CommonResponse doLogin(String username, String password) {

		return CommonResponse.createCommonResponse().setData("aliens23lsnf1s=");
	}
}
