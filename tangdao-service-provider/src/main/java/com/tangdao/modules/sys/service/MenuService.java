/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tangdao.core.constant.DataStatus;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.Menu;
import com.tangdao.modules.sys.mapper.MenuMapper;

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

	public List<Menu> findByNormal(QueryWrapper<Menu> queryWrapper) {
		queryWrapper.eq("status", DataStatus.NORMAL);
		queryWrapper.orderByAsc("sort", "id");
		return this.list(queryWrapper);
	}

	public List<Menu> findChildList(List<Menu> sourceList, List<Menu> targetList, String pid) {
		// 构建一个Map,把所有原始数据的ID作为Key,原始数据对象作为VALUE
		Map<String, Menu> dtoMap = new LinkedHashMap<String, Menu>();
		for (Menu tree : sourceList) {
			
			// 原始数据对象为Node，放入dtoMap中。
			tree.setChildren(null);
			dtoMap.put(tree.getId(), tree);
		}

		for (Map.Entry<String, Menu> entry : dtoMap.entrySet()) {
			Menu menu = entry.getValue();
			String tpid = menu.getPid();
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
