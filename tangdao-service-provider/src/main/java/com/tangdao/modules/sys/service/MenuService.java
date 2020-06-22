/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tangdao.core.service.BaseService;
import com.tangdao.core.session.SessionContext;
import com.tangdao.model.domain.Menu;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.sys.mapper.MenuMapper;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Service
public class MenuService extends BaseService<MenuMapper, Menu> {

//	@Autowired
//	private UserRoleMapper userRoleMapper;
//
//	@Autowired
//	private RoleMenuMapper roleMenuMapper;

	public List<Menu> findUserMenuList() {
//		QueryWrapper<UserRole> userRoleQw = new QueryWrapper<UserRole>();
//		userRoleQw.eq("user_id", SessionContext.getUserId());
//		List<String> roleIds = CollUtil.getFieldValues(userRoleMapper.selectList(userRoleQw), "roleId", String.class);
//		QueryWrapper<RoleMenu> roleMenuQw = new QueryWrapper<RoleMenu>();
//		roleMenuQw.in("role_id", roleIds);
//		List<String> menuIds = CollUtil.getFieldValues(roleMenuMapper.selectList(roleMenuQw), "menuId", String.class);
//		// 原始数据
//		QueryWrapper<Menu> menuQw = new QueryWrapper<Menu>();
//		menuQw.in("id", menuIds);
//		menuQw.eq("status", DataStatus.NORMAL);
//		menuQw.orderByAsc("sort");
		return super.baseMapper.findUserMenuList(SessionContext.getUserId());
	}

	public List<MenuVo> findUserMenuVoList(List<Menu> sourceList) {

		Map<String, MenuVo> dtoMap = new LinkedHashMap<String, MenuVo>();
		for (Menu menu : sourceList) {
			if (menu.getIsShow() && StrUtil.equals(MenuVo.TYPE_MENU, menu.getMenuType())) {
				// 原始数据对象为Node，放入dtoMap中。
				MenuVo menuVo = new MenuVo();
				BeanUtil.copyProperties(menu, menuVo);
				dtoMap.put(menu.getId(), menuVo);
			}
		}

		List<MenuVo> targetList = CollUtil.newLinkedList();
		for (Map.Entry<String, MenuVo> entry : dtoMap.entrySet()) {
			MenuVo menu = entry.getValue();
			String tpid = menu.getPId();
			if (dtoMap.get(tpid) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(menu);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				MenuVo parent = dtoMap.get(tpid);
				if (parent.getChildren() == null) {
					parent.setChildren(new LinkedList<MenuVo>());
				}
				parent.addChild(menu);
			}
		}

		return targetList;
	}

	public List<String> findUserPermVoList(List<Menu> sourceList) {
		// 权限列表
		List<String> premInnerList = CollUtil.newLinkedList(); // 页面内按钮权限

		// 添加基于Permission的权限信息
		sourceList.stream().filter(menu -> StrUtil.isNotBlank(menu.getPermission())).forEach(menu -> {
			// 添加基于Permission的权限信息
			for (String permission : StrUtil.split(menu.getPermission(), ",")) {
				premInnerList.add(permission);
			}
		});
		return premInnerList;
	}
}
