/**
 *
 */
package com.tangdao.core.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tangdao.core.model.ChildVo;
import com.tangdao.core.model.TreeEntity;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月9日
 */
public abstract class TreeService<M extends BaseMapper<T>, T extends TreeEntity<T>> extends BaseService<M, T>
		implements IService<T> {

	/**
	 * 
	 * TODO 递归查询子节点
	 * 
	 * @param sourceList
	 * @param targetList
	 */
	public List<ChildVo> getChildren(List<ChildVo> sourceList) {
		List<ChildVo> targetList = new LinkedList<ChildVo>();
		// 构建一个Map,把所有原始数据的ID作为Key,原始数据对象作为VALUE
		Map<String, ChildVo> dtoMap = new LinkedHashMap<String, ChildVo>();
		for (ChildVo tree : sourceList) {
			// 原始数据对象为Node，放入dtoMap中。
			tree.setChildren(null);
			dtoMap.put(tree.getId(), tree);
		}
		for (Map.Entry<String, ChildVo> entry : dtoMap.entrySet()) {
			ChildVo node = entry.getValue();
			String tParentId = node.getPid();
			if (dtoMap.get(tParentId) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(node);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				ChildVo parent = dtoMap.get(tParentId);
				if (parent.getChildren() == null) {
					parent.setChildren(CollUtil.newArrayList());
				}
				parent.addChild(node);
			}
		}
		return targetList;
	}
}
