/**
 *
 */
package com.tangdao.core.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tangdao.core.config.Global;
import com.tangdao.core.model.ChildVo;
import com.tangdao.core.model.TreeEntity;
import com.tangdao.core.utils.ColumnUtil;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

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
	 * 获取TreeList数据
	 * 
	 * @param queryWrapper
	 * @return
	 */
	public List<ChildVo> getTreeList(Wrapper<T> queryWrapper) {
		List<T> sourceList = super.list(queryWrapper);
		if (CollUtil.isEmpty(sourceList)) {
			return CollUtil.newArrayList();
		}
		List<ChildVo> list = sourceList.stream().map(node -> {
			ChildVo nodeVo = new ChildVo();
			nodeVo.setId((String) ColumnUtil.getPKAttrVal(node));
			nodeVo.setPid(node.getPid());
			nodeVo.setLabel((String) ColumnUtil.getTreeNameAttrVal(node));
			return nodeVo;
		}).collect(Collectors.toList());
		return getChildren(list);
	}

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

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean save(T entity) {
		if (null == entity) {
			return false;
		}
		if (StrUtil.isEmpty(entity.getPid()) || Global.ROOT_ID.equals(entity.getPid())) {
			entity.setPid(Global.ROOT_ID);
			entity.setPids(entity.getPid() + ",");
			entity.setTreeNames(ColumnUtil.getTreeNameAttrVal(entity) + StrUtil.EMPTY);
		} else {
			T pDictData = super.getById(entity.getPid());
			entity.setPids(pDictData.getPids() + entity.getPid() + ",");
			entity.setTreeNames(pDictData.getTreeNames() + "/" + ColumnUtil.getTreeNameAttrVal(entity));
		}

		if (null == entity.getTreeSort()) {
			entity.setTreeSort(10);
		}
		return super.save(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean updateById(T entity) {
		String id = null;
		if (null == entity || StrUtil.isEmpty(id = ObjectUtil.toString(ColumnUtil.getPKAttrVal(entity)))) {
			return false;
		}
		T oldEntity = getById(id);
		if (oldEntity == null) {
			return false;
		}
		if (StrUtil.isEmpty(entity.getPid()) || Global.ROOT_ID.equals(entity.getPid())) {
			entity.setPid(Global.ROOT_ID);
			entity.setPids(entity.getPid() + ",");
			entity.setTreeNames(ColumnUtil.getTreeNameAttrVal(entity) + StrUtil.EMPTY);
		} else {
			T pDictData = super.getById(entity.getPid());
			entity.setPids(pDictData.getPids() + entity.getPid() + ",");
			entity.setTreeNames(pDictData.getTreeNames() + "/" + ColumnUtil.getTreeNameAttrVal(entity));
		}
		// 更新对象
		super.updateById(entity);
		// 更新子节点
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like(Global.FieldName.pids.name(), id);
		List<T> children = super.list(queryWrapper);
		for (T t : children) {
			if (t.getPids() != null && oldEntity.getPid() != null) {
				t.setPids(t.getPids().replace(oldEntity.getPids(), entity.getPids()));
				t.setTreeNames(t.getTreeNames().replace(oldEntity.getTreeNames(), entity.getTreeNames()));
				super.updateById(t);
			}
		}
		return true;
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean deleteyIdAndChildren(T entity) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like(Global.FieldName.pids.name(), ColumnUtil.getPKAttrVal(entity));
		List<T> list = super.list(queryWrapper);
		for (T t : list) {
			super.removeById(t);
		}
		super.removeById(entity);
		return true;
	}
}
