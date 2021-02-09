/**
 *
 */
package com.tangdao.system.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.tangdao.core.service.TreeService;
import com.tangdao.system.mapper.MenuMapper;
import com.tangdao.system.model.domain.Menu;

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
public class MenuService extends TreeService<MenuMapper, Menu> {

	/**
	 * 
	 * TODO 根据角色获取菜单
	 * 
	 * @param menu
	 * @return
	 */
	public List<Menu> findByRoleCode(String roleCode) {
		Assert.notEmpty(roleCode, "异常：roleCode 不可以为空!");
		Menu menu = new Menu();
		menu.setRoleCode(roleCode);
		return this.baseMapper.findByRoleMenu(menu);
	}

	/**
	 * 
	 * TODO 根据用户获取菜单
	 * 
	 * @param userCode
	 * @param defaultRoleCodes 默认角色代码
	 * @return
	 */
	public List<Menu> findByUserCode(String userCode, List<String> defaultRoleCodes) {
		Assert.notEmpty(userCode, "异常：userCode 不可以为空!");
		Menu menu = new Menu();
		menu.setUserCode(userCode);
		menu.setDefaultRoleCodes(defaultRoleCodes);
		return this.baseMapper.findByUserMenu(menu);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean saveOrUpdate(Menu menu) {
		if (menu == null) {
			return false;
		}
		if (StrUtil.isBlank(menu.getParentCode()) || StrUtil.equals(Menu.ROOT_CODE, menu.getParentCode())) {
			menu.setParentCode(Menu.ROOT_CODE);
		}
		// 父级节点对象
		Menu parentMenu = this.getById(menu.getParentCode());
		if (parentMenu == null) {
			parentMenu = new Menu();
			parentMenu.setMenuCode(Menu.ROOT_CODE);
			parentMenu.setParentCodes(StrUtil.EMPTY);
			parentMenu.setTreeNames(StrUtil.EMPTY);
		}

		Menu oldMenu = this.getById(menu.getMenuCode());
		String oldParentCodes = menu.getParentCodes();
		String oldTreeNames = menu.getTreeNames();

		menu.setParentCodes(parentMenu.getParentCodes() + parentMenu.getMenuCode() + ",");
		menu.setTreeNames(StrUtil.isBlank(parentMenu.getTreeNames()) ? menu.getMenuName()
				: parentMenu.getTreeNames() + "/" + menu.getMenuName());

		boolean result = false;
		if (StrUtil.isBlank(menu.getMenuCode()) || oldMenu == null) {
			if (menu.getTreeSort() == null) {
				menu.setTreeSort(Menu.DEFAULT_TREE_SORT);
			}
			result = this.save(menu);
		} else {
			result = this.updateById(menu);
		}
		QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
		queryWrapper.like("parent_codes", menu.getMenuCode());
		List<Menu> list = this.list(queryWrapper);
		list.stream().filter(e -> e.getParentCodes() != null && oldParentCodes != null).forEach(item -> {
			item.setParentCodes(item.getParentCodes().replace(oldParentCodes, menu.getParentCodes()));
			item.setTreeNames(item.getTreeNames().replace(oldTreeNames, menu.getTreeNames()));
			this.updateById(item);
		});
		return result;
	}

}
