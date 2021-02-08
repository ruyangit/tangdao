/**
 *
 */
package com.tangdao.web.controller.system;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.system.model.domain.Menu;
import com.tangdao.system.service.MenuService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月23日
 */
@RestController
@RequestMapping(value = { "v1/system" })
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

	/**
	 * 
	 * TODO
	 * 
	 * @param excludeCode 排除的节点
	 * @param isShowCode  是否显示编码
	 * @return
	 */
	@GetMapping("/queryMenuTreeData")
	public CommonResponse getMenuTreeData(Menu menu, String excludeCode, String isShowCode) {
		QueryWrapper<Menu> queryWrapper = new QueryWrapper<Menu>();
		if (StrUtil.isNotBlank(excludeCode)) {
			queryWrapper.ne("menu_code", excludeCode);
			queryWrapper.notLike("parent_codes", excludeCode);
		}

		if (StrUtil.isNotBlank(menu.getIsShow())) {
			queryWrapper.eq("is_show", menu.getIsShow());
		}
		queryWrapper.ne("status", Menu.DELETE);
		queryWrapper.orderByAsc("tree_sort");
		List<Menu> sourceList = menuService.list(queryWrapper);
		Map<String, Menu> menuMap = new HashMap<String, Menu>();
		sourceList.stream().forEach(tree -> {
			tree.setMenuName(tree.getTreeNodeName(isShowCode, tree.getMenuCode(), tree.getMenuName()));
			tree.setChildren(null);
			menuMap.put(tree.getMenuCode(), tree);
		});
		// 转换为 treeData
		List<Menu> targetList = new LinkedList<Menu>();
		menuMap.entrySet().forEach(tree -> {
			Menu temp = tree.getValue();
			if (menuMap.get(temp.getParentCode()) == null) {
				targetList.add(temp);
			} else {
				Menu tempParent = menuMap.get(temp.getParentCode());
				if (CollUtil.isEmpty(tempParent.getChildren())) {
					tempParent.setChildren(new LinkedList<Menu>());
				}
				tempParent.addChild(temp);
			}
		});

		return success(targetList);
	}
}
