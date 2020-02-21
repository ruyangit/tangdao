/**
 * 
 */
package com.tangdao.framework.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.tangdao.common.exception.ServiceException;
import com.tangdao.framework.persistence.TreeEntity;
import com.tangdao.framework.service.ITreeService;

import cn.hutool.core.collection.CollUtil;

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
		sourceList.stream().filter(tree -> tree.getParentCode() != null && tree.getParentCode().equals(parentId))
		.forEach(tree -> {
			targetList.add(tree);
			// 判断是否还有子节点, 有则继续获取子节点
			sourceList.stream().filter(
					child -> child.getParentCode() != null && child.getParentCode().equals(tree.getPkValue()))
					.peek(child -> convertTreeSort(targetList, sourceList, tree.getPkValue())).findFirst();
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
			dtoMap.put(tree.getPkValue(), tree);
		}

		for (Map.Entry<String, T> entry : dtoMap.entrySet()) {
			T node = entry.getValue();
			String tParentId = node.getParentCode();
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
	
	public boolean updateTreeLeaf(T entity) {
		if (StringUtils.isBlank(entity.getPkValue())) {	
            return false;	
        }
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		StringBuffer sql = new StringBuffer();
		sql.append(" tree_leaf = (");
		sql.append(" select tree_leaf from(");
		sql.append(" SELECT (case when count(1) > 0 then '0' else '1' end) tree_leaf");
		sql.append(" from " + tableInfo.getTableName());
		sql.append(" where status='0'").append(" and parent_code = '").append(entity.getPkValue()).append("'");
		sql.append(" ) a )");
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(sql.toString());
		updateWrapper.eq(tableInfo.getKeyColumn(), entity.getPkValue());
		return this.update(updateWrapper);
	}

	@Override
	public boolean updateTreeSort(T entity) {
		if (StringUtils.isBlank(entity.getPkValue())) {	
            return false;	
        }
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(" tree_sort = " + entity.getTreeSort());
		updateWrapper.eq(tableInfo.getKeyColumn(), entity.getPkValue());
		return this.update(updateWrapper);
	}
	
	@Override
	public boolean deleteChild(T entity) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(entity.getPkValue())) {	
            return false;	
        }
		this.removeById(entity.getPkValue());
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like("parent_codes", entity.getPkValue());
		this.remove(queryWrapper);
		T parentEntity = this.getById(entity.getParentCode());
		if (parentEntity != null && parentEntity.getPkValue()!=null) {	
            this.updateTreeLeaf(parentEntity);	
        }
		return true;
	}
	
	@Override
	public boolean saveOrUpdate(T entity) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(entity.getPkValue())) {	
            return false;	
        }
		//旧的对象
		T oldEntity = this.getById(entity.getPkValue());
		T parentEntity = null;
		if (StringUtils.isNotBlank(entity.getParentCode()) && !TreeEntity.ROOT_CODE.equals(entity.getParentCode())) {
			parentEntity = this.getById(entity.getParentCode());
		}
		//实例一个新的
		Class<T> entityClass = this.currentModelClass();
		if (parentEntity == null || StringUtils.isBlank(entity.getParentCode()) || TreeEntity.ROOT_CODE.equals(entity.getParentCode())) {
			try {
				// 构造一个父节点
				parentEntity = entityClass.getConstructor(String.class).newInstance(TreeEntity.ROOT_CODE);
			} catch (Exception e) {
				throw new ServiceException("初始化父类对象", e);
			}
			parentEntity.setParentCodes(StringUtils.EMPTY);
			parentEntity.setTreeNames(StringUtils.EMPTY);

			// 设置保存对象的父节点
			entity.setParentCode(parentEntity.getPkValue());
		}
		//设置父类对象

		String oldParentCodes = entity.getParentCodes();
		String oldTreeNames = entity.getTreeNames();

		entity.setParentCodes(parentEntity.getParentCodes() + parentEntity.getPkValue() + ",");
		entity.setTreeLevel(String.valueOf(entity.getParentCodes().replaceAll("[^,]", "").length() - 1));

		String treeId = entity.getPkValue();
		String treeName = entity.getTreeName_();

		if (treeName == null) {
			treeName = StringUtils.EMPTY;
		}

		if (entity.isRoot()) {
			entity.setTreeNames(treeName);
		} else {
			entity.setTreeNames(parentEntity.getTreeNames() + "/" + treeName);
		}

		if (StringUtils.isBlank(treeId)) {
			this.save(entity);
		} else {
			this.updateById(entity);
		}
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like("parent_codes", entity.getPkValue());
		List<T> list = this.list(queryWrapper);
		for (T e : list) {
			if (e.getParentCodes() != null && oldParentCodes != null) {
				e.setParentCodes(e.getParentCodes().replace(oldParentCodes, entity.getParentCodes()));
				e.setTreeNames(e.getTreeNames().replace(oldTreeNames, entity.getTreeNames()));
				e.setTreeLevel(String.valueOf(e.getParentCodes().replaceAll("[^,]", "").length() - 1));
				this.updateById(e);
			}
		}
		//更新原始父节点对象的叶子状态
		if (oldEntity != null 
				&& oldEntity.getParentCode()!=null
				&& !TreeEntity.ROOT_CODE.equals(oldEntity.getParentCode())
				&& !StringUtils.equals(entity.getParentCode(), oldEntity.getParentCode())) {
			try {
				// 构造一个父节点
				parentEntity = entityClass.getConstructor(String.class).newInstance(TreeEntity.ROOT_CODE);
			} catch (Exception e) {
				throw new ServiceException("初始化父类对象", e);
			}
			parentEntity.setPkValue(oldEntity.getParentCode());
			this.updateTreeLeaf(parentEntity);
		}
		return true;
	}
}
