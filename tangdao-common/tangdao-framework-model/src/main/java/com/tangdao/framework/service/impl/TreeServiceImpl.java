/**
 * 
 */
package com.tangdao.framework.service.impl;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.tangdao.framework.model.entity.TreeEntity;
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
					child -> child.getParentCode() != null && child.getParentCode().equals(tree.getKey()))
					.peek(child -> convertTreeSort(targetList, sourceList, tree.getKey())).findFirst();
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
			dtoMap.put(tree.getKey(), tree);
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
	
	public void updateTreeLeaf(T entity) {
		if (TreeEntity.ROOT_CODE.equals(entity.getKey())) {
			return;
		}
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		StringBuffer sql = new StringBuffer();
		sql.append(" tree_leaf = (");
		sql.append(" select tree_leaf from(");
		sql.append(" SELECT (case when count(1) > 0 then '0' else '1' end) tree_leaf");
		sql.append(" from " + tableInfo.getTableName());
		sql.append(" where status='0'").append(" and parent_code = '").append(entity.getKey()).append("'");
		sql.append(" ) a )");
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(sql.toString());
		updateWrapper.eq(tableInfo.getKeyColumn(), entity.getKey());
		this.update(updateWrapper);
	}

	@Override
	public void updateTreeSort(T entity) {
		// TODO Auto-generated method stub
		if (entity.getTreeSort() == null) {	
            entity.setTreeSort(new BigDecimal(0));	
        }
		TableInfo tableInfo = TableInfoHelper.getTableInfo(entity.getClass());
		UpdateWrapper<T> updateWrapper = new UpdateWrapper<T>();
		updateWrapper.setSql(" tree_sort = " + entity.getTreeSort());
		updateWrapper.eq(tableInfo.getKeyColumn(), entity.getKey());
		this.update(updateWrapper);
	}

	@Override
	public boolean remove(T entity) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(entity.getKey())) {	
            return false;	
        }
		this.removeById(entity.getKey());
		QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
		queryWrapper.like("parent_codes", entity.getKey());
		this.remove(queryWrapper);
		
		T parent = this.getById(entity.getParentCode());
		if (parent != null && parent.getKey()!=null) {	
            this.updateTreeLeaf(parent);	
        }
		return true;
	}
}
