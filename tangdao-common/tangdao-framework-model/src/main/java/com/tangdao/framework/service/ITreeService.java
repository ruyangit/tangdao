/**
 * 
 */
package com.tangdao.framework.service;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
public interface ITreeService<T> extends ICrudService<T> {

	/**
	 * 树形结构数据排序
	 * @param sourceList 源数据列表
	 * @param targetList 目标数据列表
	 * @param parentId   目标数据列表的顶级节点
	 */
	void convertTreeSort(List<T> sourceList, List<T> targetList, String parentId);

	/**
	 * 普通股集合组装为树形结构
	 * @param sourceList 源数据列表
	 * @param targetList 目标数据列表
	 * @param parentId   目标数据列表的顶级节点
	 */
	void convertChildList(List<T> sourceList, List<T> targetList, String parentId);
	
	/**
	 * 更新排序字段
	 * @param entity
	 */
	boolean updateTreeSort(T entity);
	
	/**
	 * 删除子节点
	 * @param entity
	 * @return
	 */
	boolean removeChildById(Serializable id);
	
}
