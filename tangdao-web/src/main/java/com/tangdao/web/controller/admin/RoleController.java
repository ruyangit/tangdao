/**
 *
 */
package com.tangdao.web.controller.admin;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.web.BaseController;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@RestController
@RequestMapping(value = { "/admin" })
public class RoleController extends BaseController {

//	@Autowired
//	private RoleService roleService;
//
//	@Autowired
//	private MenuService menuService;
//	
//	@GetMapping("/roles")
//	public CommonResponse page(Page<Role> page, String roleName) {
//		QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
//		if (StrUtil.isNotBlank(roleName)) {
//			queryWrapper.like("role_name", roleName);
//		}
//		return success(roleService.page(page, queryWrapper));
//	}
//
//	@GetMapping("/role-list")
//	public CommonResponse list(String roleName) {
//		QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
//		if (StrUtil.isNotBlank(roleName)) {
//			queryWrapper.like("role_name", roleName);
//		}
//		queryWrapper.eq("status", DataStatus.NORMAL);
//		return success(roleService.list(queryWrapper));
//	}
//
//	@Validate({ @Field(name = "id", rules = { @Rule(message = "查询主键不能为空") }) })
//	@GetMapping("/role-detail")
//	public CommonResponse detail(String id) {
//		Role role = roleService.getById(id);
//		Map<String, Object> data = MapUtil.newHashMap();
//		data.put("role", role);
//
//		List<String> menuIds = CollUtil.getFieldValues(menuService.findRoleMenuList(id), "id", String.class);
//		data.put("menuIds", menuIds);
//		return success(data);
//	}
//
//	@Validate({ @Field(name = "role.roleName", rules = { @Rule(message = "角色名不能为空") }) })
//	@PostMapping("/roles")
//	public CommonResponse saveRole(@RequestBody Role role) {
//		Role er = roleService.findByRoleName(role.getRoleName());
//		if (er != null) {
//			throw new IllegalArgumentException("角色 '" + er.getRoleName() + "' 已存在");
//		}
//		return success(roleService.save(role));
//	}
//	
////	@PostMapping("/role-menus")
////	public CommonResponse saveRoleMenu(@RequestBody RoleMenuDTO roleMenuDTO) {
////		return success(roleService.saveRoleMenu(roleMenuDTO));
////	}
//
//	@PostMapping("/role-update")
//	public CommonResponse updateRole(@RequestBody RoleDTO roleDto) {
//		Role role = new Role();
//		BeanUtil.copyProperties(roleDto, role);
//
//		if (!Validator.equal(roleDto.getRoleName(), roleDto.getOldRoleName())
//				&& roleService.count(Wrappers.<Role>lambdaQuery().eq(Role::getRoleName, roleDto.getRoleName())) > 0) {
//			throw new IllegalArgumentException("角色 '" + roleDto.getRoleName() + "' 已存在");
//		}
//
////		if (roleService.checkUserRoleRef(roleDto)) {
////			throw new BusinessException(CommonApiCode.FAIL, "操作失败，存在未解除的关联数据");
////		}
//		
//		roleService.saveRoleMenu(roleDto);
//
//		return success(roleService.updateById(role));
//	}
//
//	@PostMapping("/role-delete")
//	public CommonResponse deleteRole(@RequestBody RoleDTO roleDto) {
//		if (roleService.checkUserRoleRef(roleDto)) {
//			throw new BusinessException(CommonApiCode.FAIL, "操作失败，存在未解除的关联数据");
//		}
//		return success(roleService.deleteRole(roleDto.getId()));
//	}
//	
//	@GetMapping("/role-policies")
//	public CommonResponse policies(String roleId) {
//		return success(roleService.findRolePolicy(roleId));
//	}
//	
//	@PostMapping("/role-policies")
//	public CommonResponse policies(@RequestBody PolicyDTO policyDTO) {
//		return success(roleService.saveRolePolicy(policyDTO));
//	}
}
