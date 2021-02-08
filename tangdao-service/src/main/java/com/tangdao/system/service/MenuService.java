/**
 *
 */
package com.tangdao.system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.tangdao.core.service.TreeService;
import com.tangdao.system.mapper.MenuMapper;
import com.tangdao.system.model.domain.Menu;

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

}
