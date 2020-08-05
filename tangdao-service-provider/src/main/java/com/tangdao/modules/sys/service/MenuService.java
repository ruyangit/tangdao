/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.common.constant.CommonContext;
import com.tangdao.core.service.BaseService;
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

	public List<MenuVo> findMenuVoChildList() {
		List<MenuVo> sourceList = this.baseMapper.findMenuVoList(new MenuVo());
		Map<String, MenuVo> dtoMap = new LinkedHashMap<String, MenuVo>();
		for (MenuVo menu : sourceList) {
			menu.setChildren(null);
			dtoMap.put(menu.getId(), menu);
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

	public List<Menu> findRoleMenuList(String roleId) {
		return super.baseMapper.findRoleMenuList(roleId);
	}

	@Cacheable(value = CacheService.RED_USER_MENU, key = "#userId")
	public List<Menu> findUserMenuList(String userId) {
		return super.baseMapper.findUserMenuList(userId);
	}

	/**
	 * 
	 * @param sourceList
	 * @param isAll      控制是否显示所有节点 （true 显示所有节点）
	 * @return
	 */
	public List<MenuVo> findUserMenuVoList(List<Menu> sourceList, boolean isAll) {

		Map<String, MenuVo> dtoMap = new LinkedHashMap<String, MenuVo>();
		for (Menu menu : sourceList) {
			if (!isAll) {
				if (menu.getIsShow().equals(CommonContext.YES)
						&& StrUtil.equals(MenuVo.TYPE_MENU, menu.getMenuType())) {
					// 原始数据对象为Node，放入dtoMap中。
					MenuVo menuVo = new MenuVo();
					BeanUtil.copyProperties(menu, menuVo);
					menuVo.setName(menu.getMenuName());
					menuVo.setPath(menu.getMenuPath());
					dtoMap.put(menu.getId(), menuVo);
				}
			} else {
				MenuVo menuVo = new MenuVo();
				BeanUtil.copyProperties(menu, menuVo);
				menuVo.setName(menu.getMenuName());
				menuVo.setPath(menu.getMenuPath());
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

	public List<Menu> findList(String pId) {
		if (StrUtil.isEmpty(pId)) {
			return this.list();
		}
		return this.list(Wrappers.<Menu>lambdaQuery().eq(Menu::getPId, pId).or().eq(Menu::getId, pId));
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(Menu menu) {
		if (null != menu) {

			Menu parentMenu = null;
			if (!StringUtils.checkValNull(menu.getPId())) {
				parentMenu = super.getById(menu.getPId());
			}
			if (parentMenu == null) {
				parentMenu = new Menu();
				parentMenu.setId(MenuVo.ROOT_ID);
				parentMenu.setPIds(StringUtils.EMPTY);
				menu.setPId(parentMenu.getId());
			}
			Menu oldMenu = super.getById(menu.getId());
			
			menu.setPIds(parentMenu.getPIds() + parentMenu.getId() + ",");
			menu.setModified(new Date());
			super.saveOrUpdate(menu);

			List<Menu> children = this.list(Wrappers.<Menu>lambdaQuery().like(Menu::getPIds, menu.getId()));
			children.stream().filter(e -> e.getPIds() != null && oldMenu != null).forEach(e -> {
				e.setPIds(e.getPIds().replace(oldMenu.getPIds(), menu.getPIds()));
				e.setModified(new Date());
				this.updateById(e);
			});
			return true;
		}
		return false;
	}
}
