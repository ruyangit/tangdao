/**
 *
 */
package com.tangdao.portal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LoginUser;
import com.tangdao.core.model.vo.SessionUser;
import com.tangdao.core.web.BaseController;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@GetMapping("profile")
	public CommonResponse userProfile(@LoginUser SessionUser user) {
		System.out.println(JSON.toJSONString(user));
		return success(null);
	}
}
