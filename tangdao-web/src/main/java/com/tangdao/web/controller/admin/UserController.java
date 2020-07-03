/**
 *
 */
package com.tangdao.web.controller.admin;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.exception.BusinessException;
import com.tangdao.core.annotation.AuditLog;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.core.session.SessionContext;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.aspect.model.Log;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.Menu;
import com.tangdao.model.domain.User;
import com.tangdao.model.dto.UserDTO;
import com.tangdao.model.dto.UserRoleDTO;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.sys.service.LogService;
import com.tangdao.modules.sys.service.MenuService;
import com.tangdao.modules.sys.service.UserService;
import com.tangdao.web.config.TangdaoProperties;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

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
	private MenuService menuService;

	@Autowired
	private LogService logService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TangdaoProperties properties;

	@GetMapping
	public CommonResponse page(Pageinfo page, UserDTO user) {
		IPage<Map<String, Object>> pageinfo = userService.findMapsPage(page, user);
		pageinfo.getRecords().forEach(e -> {
			e.put("isa", properties.getUser().isSuperAdmin(String.valueOf(e.get("username"))));
		});
		return success(pageinfo);
	}

	@GetMapping("/detail")
	@AuditLog(title = "用户详情", operation = "'访问【'+#username+'】详情信息接口'")
	public CommonResponse detail(String username) {
		User user = userService.findByUsername(username);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("user", user);
		data.put("isa", properties.getUser().isSuperAdmin(username));
		return success(data);
	}

	@Validate({ @Field(name = "user.username", rules = { @Rule(message = "账号不能为空") }),
			@Field(name = "user.password", rules = { @Rule(message = "密码不能为空") }) })
	@PostMapping
	public CommonResponse saveUser(@RequestBody UserDTO user) {
		User eu = userService.findByUsername(user.getUsername());
		if (eu != null) {
			throw new IllegalArgumentException("用户 '" + eu.getUsername() + "' 已存在");
		}
		return success(userService.saveUserAndRoleIds(user.getUsername(), passwordEncoder.encode(user.getPassword()),
				user.getRoleIds()));
	}

	@Validate({ @Field(name = "user.password", rules = { @Rule(message = "密码不能为空") }) })
	@PostMapping("/password/modify")
	public CommonResponse passwordModify(@RequestBody User user) {
		return success(userService.passwordModify(user.getId(), passwordEncoder.encode(user.getPassword())));
	}

	@PostMapping("/update")
	public CommonResponse updateUser(@RequestBody UserDTO userDto) {
		User user = new User();
		BeanUtil.copyProperties(userDto, user);

		if (!Validator.equal(userDto.getUsername(), userDto.getOldUsername())
				&& userService.count(Wrappers.<User>lambdaQuery().eq(User::getUsername, userDto.getUsername())) > 0) {
			throw new IllegalArgumentException("用户 '" + userDto.getUsername() + "' 已存在");
		}
		user.setPassword(null); // 密码不更新
		return success(userService.updateById(user));
	}

	@PostMapping("/delete")
	public CommonResponse deleteUser(@RequestBody UserDTO user) {
		if (user.getId().equals(SessionContext.getUserId())) {
			throw new BusinessException(CommonApiCode.FAIL, "操作失败，不可以删除用户自己");
		}
		return success(userService.deleteUser(user.getId()));
	}

	@GetMapping("/role")
	public CommonResponse userRole(UserRoleDTO userRole) {
		if (StrUtil.isEmpty(userRole.getUsername()) && StrUtil.isEmpty(userRole.getUserId())
				&& StrUtil.isEmpty(userRole.getRoleId())) {
			throw new IllegalArgumentException("参数不能为空");
		}
		return success(userService.findUserRoleMapsList(userRole));
	}

	@PostMapping("/role")
	public CommonResponse saveUserRole(@RequestBody UserRoleDTO userRole) {
		return success(userService.saveUserRole(userRole));
	}

	@Validate({ @Field(name = "userRole.id", rules = { @Rule(message = "删除主键不能为空") }) })
	@PostMapping("role/delete")
	public CommonResponse deleteUserRole(@RequestBody UserRoleDTO userRole) {
		return success(userService.deleteUserRole(userRole));
	}

	@GetMapping("/menu")
	public CommonResponse userMenu(String userId) {
		List<Menu> sourceList = menuService.findUserMenuList(userId);
		List<MenuVo> menuVoList = menuService.findUserMenuVoList(sourceList, true);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("menuList", menuVoList);
		return success(data);
	}

	@GetMapping("/log")
	public CommonResponse logPage(Page<Log> page) {
		return success(logService.findPage(page));
	}
}
