/**
 *
 */
package com.tangdao.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.constant.OpenApiCode;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.model.dto.UserDTO;
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

	@PostMapping("/save")
	public CommonResponse save(@RequestBody UserDTO userDTO) {
		if (userDTO.getUser() == null) {
			return fail(CommonApiCode.COMMON_REQUEST_EXCEPTION);
		}
		if (userService.checkUsernameExists(userDTO.getOldUsername(), userDTO.getUser().getUsername())) {
			return fail(CommonApiCode.COMMON_REQUEST_EXCEPTION)
					.message("保存用户'" + userDTO.getUser().getUsername() + "'失败，登录账号已存在");
		}
		return success(userService.save(userDTO.getUser()));
	}
}
