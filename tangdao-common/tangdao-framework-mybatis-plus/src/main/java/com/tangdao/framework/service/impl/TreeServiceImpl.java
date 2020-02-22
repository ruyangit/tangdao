/**
 * 
 */
package com.tangdao.framework.service.impl;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.tangdao.framework.exception.ServiceException;
import com.tangdao.framework.persistence.TreeEntity;
import com.tangdao.framework.service.ITreeService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
public class TreeServiceImpl<M extends BaseMapper<T>, T extends TreeEntity> extends CrudServiceImpl<M, T> implements ITreeService<T> {

	@Override
	public void convertTreeSort(List<T> sourceList, List<T> targetList, String parentId) {
		// TODO Auto-generated method stub
		sourceList.stream().filter(tree -> tree.getParentId() != null && tree.getParentId().equals(parentId))
		.forEach(tree -> {
			targetList.add(tree);
			String pkValue = getPkValue(tree);
			// 判断是否还有子节点, 有则继续获取子节点
			sourceList.stream().filter(
					child -> child.getParentId() != null && child.getParentId().equals(pkValue))
					.peek(child -> convertTreeSort(targetList, sourceList, pkValue)).findFirst();
		});
	}

	@Override
	public void convertChildList(List<T> sourceList, List<T> targetList, String parentId) {
		// TODO Auto-generated method stub
		// 构建一个Map,把所有原始数据的ID作为Key,原始数据对象作为VALUE
		Map<String, T> dtoMap = new LinkedHashMap<String, T>();
		for (T tree : sourceList) {
			// 原始数据对象为Node，放入dtoMap中。
			tree.setChildren(null);
			dtoMap.put(getPkValue(tree), tree);
		}

		for (Map.Entry<String, T> entry : dtoMap.entrySet()) {
			T node = entry.getValue();
			String tParentId = node.getParentId();
			if (dtoMap.get(tParentId) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(node);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				T parent = dtoMap.get(tParentId);
				if (parent.getChildren() == null) {
					parent.setChildren(CollUtil.newArrayList());
				}
				parent.addChild(node);
			}
		}
	}
	
	@Override
	public boolean updateTreeSort(T entity) {
		String pkValue = getPkValue(entity);
		if (StringUtils.isBlank(pkValue)) {	
            return false;	
        }
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(" tree_sort = " + entity.getTreeSort());
		updateWrapper.eq(tableInfo.getKeyColumn(), pkValue);
		return this.update(updateWrapper);
	}
	
	@Override
	public boolean removeChildById(Serializable id) {
		// TODO Auto-generated method stub
		T entity = this.getById(id);
		if(entity == null) {
			return false;
		}
		//删除节点
		this.removeById(id);
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like("parent_ids", id);
		this.remove(queryWrapper);
		//更新父节点的状态
		this.updateTreeLeaf(entity.getParentId());	
		return true;
	}
	
	@Override
	public boolean saveOrUpdate(T entity) {
		// TODO Auto-generated method stub
		String pkValue = getPkValue(entity);
		if (StringUtils.isBlank(pkValue)) {	
            return false;	
        }
		//构造父节点
		T parentEntity = null;
		if (StringUtils.isNotBlank(entity.getParentId()) && !TreeEntity.ROOT_ID.equals(entity.getParentId())) {
			parentEntity = this.getById(entity.getParentId());
		}
		//实例一个新的
		Class<T> entityClass = this.currentModelClass();
		if (parentEntity == null || StringUtils.isBlank(entity.getParentId()) || TreeEntity.ROOT_ID.equals(entity.getParentId())) {
			try {
				// 构造一个父节点
				parentEntity = entityClass.getConstructor(String.class).newInstance(TreeEntity.ROOT_ID);
			} catch (Exception e) {
				throw new ServiceException("初始化父类对象", e);
			}
			parentEntity.setParentIds(StringUtils.EMPTY);
			parentEntity.setTreeNames(StringUtils.EMPTY);

			// 设置保存对象的父节点
			entity.setParentId(getPkValue(parentEntity));
		}
		//设置父类对象

		String oldParentCodes = entity.getParentIds();
		String oldTreeNames = entity.getTreeNames();

		entity.setParentIds(parentEntity.getParentIds() + getPkValue(parentEntity) + ",");
		entity.setTreeLevel(String.valueOf(entity.getParentIds().replaceAll("[^,]", "").length() - 1));

		String treeName = entity.getTreeName_();

		if (treeName == null) {
			treeName = StringUtils.EMPTY;
		}

		if (entity.isRoot()) {
			entity.setTreeNames(treeName);
		} else {
			entity.setTreeNames(parentEntity.getTreeNames() + "/" + treeName);
		}

		if (StringUtils.isBlank(pkValue)) {
			this.save(entity);
		} else {
			this.updateById(entity);
		}
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like("parent_ids", pkValue);
		List<T> list = this.list(queryWrapper);
		for (T e : list) {
			if (e.getParentIds() != null && oldParentCodes != null) {
				e.setParentIds(e.getParentIds().replace(oldParentCodes, entity.getParentIds()));
				e.setTreeNames(e.getTreeNames().replace(oldTreeNames, entity.getTreeNames()));
				e.setTreeLevel(String.valueOf(e.getParentIds().replaceAll("[^,]", "").length() - 1));
				this.updateById(e);
			}
		}
		//更新原始父节点对象的叶子状态
		T oldEntity = this.getById(pkValue);
		if (oldEntity != null 
				&& oldEntity.getParentId()!=null
				&& !TreeEntity.ROOT_ID.equals(oldEntity.getParentId())
				&& !StringUtils.equals(entity.getParentId(), oldEntity.getParentId())) {
			this.updateTreeLeaf(oldEntity.getParentId());
		}
		return true;
	}
	
	/**
	 * 更新叶子状态
	 * @param entity
	 * @return
	 */
	private boolean updateTreeLeaf(String id) {
		if (StringUtils.isBlank(id)) {	
            return false;	
        }
		TableInfo tableInfo = TableInfoHelper.getTableInfo(currentModelClass());
		StringBuffer sql = new StringBuffer();
		sql.append(" tree_leaf = (");
		sql.append(" select tree_leaf from(");
		sql.append(" SELECT (case when count(1) > 0 then '0' else '1' end) tree_leaf");
		sql.append(" from " + tableInfo.getTableName());
		sql.append(" where status='0'").append(" and parent_id = '").append(id).append("'");
		sql.append(" ) a )");
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(sql.toString());
		updateWrapper.eq(tableInfo.getKeyColumn(), id);
		return this.update(updateWrapper);
	}
	
	/**
	 * 主键值
	 * @param entity
	 * @return
	 */
	private String getPkValue(T entity) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		return (String)ReflectUtil.getFieldValue(entity, tableInfo.getKeyProperty());
	}
}
