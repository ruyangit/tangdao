/**
 *
 */
package com.tangdao.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.service.UserService;
import com.tangdao.core.web.BaseController;

import cn.hutool.core.util.StrUtil;

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

	@Autowired
	private UserService userService;

	@GetMapping("/record")
	public CommonResponse record(Page<User> page, User user) {
		LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery();
		if (StrUtil.isNotBlank(user.getUsername())) {
			queryWrapper.likeRight(User::getUsername, user.getUsername());
		}
		if (StrUtil.isNotBlank(user.getStatus())) {
			queryWrapper.eq(User::getStatus, user.getStatus());
		}
		return success(userService.page(page, queryWrapper));
	}
}
