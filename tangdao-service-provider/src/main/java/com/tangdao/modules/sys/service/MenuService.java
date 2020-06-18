/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.constant.DataStatus;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.session.SessionContext;
import com.tangdao.model.domain.Menu;
import com.tangdao.model.domain.RoleMenu;
import com.tangdao.model.domain.UserRole;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.sys.mapper.MenuMapper;
import com.tangdao.modules.sys.mapper.RoleMenuMapper;
import com.tangdao.modules.sys.mapper.UserRoleMapper;

import cn.hutool.core.collection.CollUtil;

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

	@Autowired
	private UserRoleMapper userRoleMapper;

	@Autowired
	private RoleMenuMapper roleMenuMapper;

	public List<Menu> findList() {
		QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
		queryWrapper.eq("status", DataStatus.NORMAL);
		queryWrapper.orderByAsc("sort", "id");
		return this.list(queryWrapper);
	}

	public List<MenuVo> findMenuVoList() {
		QueryWrapper<UserRole> userRoleQw = new QueryWrapper<UserRole>();
		userRoleQw.eq("user_id", SessionContext.getUserId());
		List<String> roleIds = CollUtil.getFieldValues(userRoleMapper.selectList(userRoleQw), "roleId", String.class);
		QueryWrapper<RoleMenu> roleMenuQw = new QueryWrapper<RoleMenu>();
		roleMenuQw.in("role_id", roleIds);
		List<String> menuIds = CollUtil.getFieldValues(roleMenuMapper.selectList(roleMenuQw), "roleId", String.class);
		// 原始数据
		QueryWrapper<Menu> menuQw = new QueryWrapper<Menu>();
		menuQw.in("id", menuIds);
		menuQw.eq("status", DataStatus.NORMAL);
		menuQw.orderByAsc("sort");
		List<Menu> sourceList = this.list(menuQw);

		// 权限列表
		List<Menu> premissionList = CollUtil.newLinkedList();
		Map<String, Menu> dtoMap = new LinkedHashMap<String, Menu>();

		for (Menu menu : sourceList) {
			if (!menu.show() && MenuVo.TYPE_PERM.equals(menu.getMenuType())) {
				premissionList.add(menu);
				continue;
			}
			// 原始数据对象为Node，放入dtoMap中。
			menu.setChildren(null);
			dtoMap.put(menu.getId(), menu);
		}

		return null;
	}

	/**
	 * 
	 * @param sourceList
	 * @param targetList
	 * @param isAll:     true-显示所有, false-根据is_show字段控制. 默认 true
	 * @return
	 */
	public List<Menu> findChildList(List<Menu> sourceList, boolean isAll) {

		// 构建一个Map,把所有原始数据的ID作为Key,原始数据对象作为VALUE
		Map<String, Menu> dtoMap = new LinkedHashMap<String, Menu>();
		for (Menu tree : sourceList) {
			if (!isAll && !tree.show()) {
				continue;
			}
			// 原始数据对象为Node，放入dtoMap中。
			tree.setChildren(null);
			dtoMap.put(tree.getId(), tree);
		}
		List<Menu> targetList = CollUtil.newLinkedList();
		for (Map.Entry<String, Menu> entry : dtoMap.entrySet()) {
			Menu menu = entry.getValue();
			String tpid = menu.getPId();
			if (dtoMap.get(tpid) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(menu);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				Menu parent = dtoMap.get(tpid);
				if (parent.getChildren() == null) {
					parent.setChildren(new LinkedList<Menu>());
				}
				parent.addChild(menu);
			}
		}
		return targetList;
	}
}
