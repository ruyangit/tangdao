/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.User;
import com.tangdao.service.model.dto.UserDTO;
import com.tangdao.service.provider.UserService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 用户接口
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

	@LogOpt(logTitle = "获取列表分页数据")
	@GetMapping("/page")
	public CommonResponse page(Page<User> page, User user) {
		LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery();
		if (StrUtil.isNotBlank(user.getUsername())) {
			queryWrapper.likeRight(User::getUsername, user.getUsername());
		}
		if (StrUtil.isNotBlank(user.getStatus())) {
			queryWrapper.eq(User::getStatus, user.getStatus());
		}
		return renderResult(userService.page(page, queryWrapper));
	}

	@LogOpt(logTitle = "新增或更新数据")
	@PostMapping("/save")
	public CommonResponse save(@RequestBody UserDTO user) {
		if (!userService.checkUsernameExists(user.getOldUsername(), user.getUsername())) {
			return renderResult(Global.FALSE, "保存用户'" + user.getUsername() + "'失败，登录账号已存在");
		}
		if (StrUtil.isEmpty(user.getId())) {
			BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
			if (StrUtil.isEmpty(user.getPassword())) {
				user.setPassword("123456");
			}
			user.setPassword(bpe.encode(user.getPassword()));
			userService.createUser(user);
		} else {
			userService.updateById(user);
		}
		return renderResult(Global.TRUE, "保存成功");
	}

	@LogOpt(logTitle = "根据主键删除数据")
	@PostMapping("/delete")
	public CommonResponse delete(@RequestBody UserDTO user) {
		userService.deleteUser(user);
		return renderResult(Global.TRUE, "刪除成功");
	}
}
