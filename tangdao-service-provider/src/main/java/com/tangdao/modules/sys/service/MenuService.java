/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		List<String> menuIds = CollUtil.getFieldValues(roleMenuMapper.selectList(roleMenuQw), "menuId", String.class);
		// 原始数据
		QueryWrapper<Menu> menuQw = new QueryWrapper<Menu>();
		menuQw.in("id", menuIds);
		menuQw.eq("status", DataStatus.NORMAL);
		menuQw.orderByAsc("sort");
		List<Menu> sourceList = this.list(menuQw);

		// 权限列表
		List<Menu> premissionList = CollUtil.newLinkedList();
		Map<String, MenuVo> dtoMap = new LinkedHashMap<String, MenuVo>();

		for (Menu menu : sourceList) {
			if (!menu.getIsShow()) {
				premissionList.add(menu);
				continue;
			}
			// 原始数据对象为Node，放入dtoMap中。
			MenuVo menuVo = new MenuVo();
			BeanUtil.copyProperties(menu, menuVo);
			dtoMap.put(menu.getId(), menuVo);
		}

//		for (Menu menu : premissionList) {
//			if (StrUtil.isEmpty(menu.getPremission())) {
//				continue;
//			}
//			MenuVo tMenuVo = dtoMap.get(menu.getPId());
//			if (tMenuVo.getPremissions() == null) {
//				tMenuVo.setPremissions(new LinkedList<String>());
//			}
//			tMenuVo.getPremissions().add(menu.getPremission());
//		}

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
}
