/**
 *
 */
package com.tangdao.core.service;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tangdao.core.TreeEntity;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月8日
 */
public class TreeService<M extends BaseMapper<T>, T extends TreeEntity<T>> extends BaseService<M, T>
		implements IService<T> {

	/**
	 * 
	 * TODO 获取对象中的主键值
	 * 
	 * @param entity
	 * @return
	 */
	public Object getIdVal(T entity) {
		if (entity != null) {
			TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
			Assert.notNull(tableInfo, "error: can not execute. because can not find cache of TableInfo for entity!");
			String keyProperty = tableInfo.getKeyProperty();
			Assert.notEmpty(keyProperty, "error: can not execute. because can not find column for id from entity!");
			return ReflectionKit.getFieldValue(entity, tableInfo.getKeyProperty());
		}
		return null;
	}

	/**
	 * 
	 * TODO 查找节点和所有子节点
	 * 
	 * @param sourceList
	 * @param targetList
	 * @param treeCode
	 */
	public void findNodeAndChildNodeList(List<T> sourceList, List<T> targetList, String treeCode) {
		sourceList.stream().filter(tree -> tree.getParentCode() != null && tree.getParentCode().equals(treeCode))
				.forEach(tree -> {
					targetList.add(tree);
					String idVal = ObjectUtil.toString(this.getIdVal(tree));
					// 判断是否还有子节点, 有则继续获取子节点
					sourceList.stream()
							.filter(child -> child.getParentCode() != null && child.getParentCode().equals(idVal))
							.peek(child -> findNodeAndChildNodeList(targetList, sourceList, idVal)).findFirst();
				});
	}

	/**
	 * 
	 * TODO 查找节点和所有父节点
	 * 
	 * @param sourceList
	 * @param targetList
	 * @param treeCode
	 */
	public void findNodeAndParentNodeList(List<T> sourceList, List<T> targetList, String treeCode) {
		sourceList.stream().filter(tree -> {
			String idVal = ObjectUtil.toString(this.getIdVal(tree));
			return StrUtil.isNotBlank(treeCode) && StrUtil.equals(treeCode, idVal);
		}).forEach(tree -> {
			findNodeAndParentNodeList(sourceList, targetList, tree.getParentCode());
			targetList.add(tree);
		});
	}
	
	

}