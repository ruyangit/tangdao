/**
 *
 */
package com.tangdao.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.model.domain.Role;
import com.tangdao.modules.sys.service.RoleService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@RestController
@RequestMapping(value = { "/admin/roles" })
public class RoleController extends BaseController {

	@Autowired
	private RoleService roleService;

	@GetMapping
	public CommonResponse page(Page<Role> page, String roleName) {
		QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
		if (StrUtil.isNotBlank(roleName)) {
			queryWrapper.like("role_name", roleName);
		}
		return success(roleService.page(page, queryWrapper));
	}
}
