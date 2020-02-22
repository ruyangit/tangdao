/**
 * 
 */
package com.tangdao.framework.persistence;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tangdao.common.exception.ServiceException;

import cn.hutool.core.util.ReflectUtil;

/**
 * <p>
 * TODO 树形结构对象基础属性
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月21日
 */
public class TreeEntity extends DataEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
     * 父级编号
     */
	protected String parentId;

    /**
     * 所有父级编号
     */
	protected String parentIds;

    /**
     * 本级排序号（升序）
     */
	protected BigDecimal treeSort;
    
    /**
     * 节点名称
     */
    @JsonIgnore
	@TableField(exist = false)
	protected String treeName_;

    /**
     * 全节点名
     */
    protected String treeNames;

    /**
     * 是否最末级
     */
    protected String treeLeaf;

    /**
     * 层次级别
     */
    protected String treeLevel;
    
    /**
     * 子节点
     */
    @TableField(exist = false)
	protected List<Object> children;
    

	/**
	 * @return the parentId
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the parentIds
	 */
	public String getParentIds() {
		return parentIds;
	}

	/**
	 * @param parentIds the parentIds to set
	 */
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * @return the treeSort
	 */
	public BigDecimal getTreeSort() {
		return treeSort;
	}

	/**
	 * @param treeSort the treeSort to set
	 */
	public void setTreeSort(BigDecimal treeSort) {
		this.treeSort = treeSort;
	}

	/**
	 * @return the treeNames
	 */
	public String getTreeNames() {
		return treeNames;
	}

	/**
	 * @param treeNames the treeNames to set
	 */
	public void setTreeNames(String treeNames) {
		this.treeNames = treeNames;
	}

	/**
	 * @return the treeLeaf
	 */
	public String getTreeLeaf() {
		return treeLeaf;
	}

	/**
	 * @param treeLeaf the treeLeaf to set
	 */
	public void setTreeLeaf(String treeLeaf) {
		this.treeLeaf = treeLeaf;
	}

	/**
	 * @return the treeLevel
	 */
	public String getTreeLevel() {
		return treeLevel;
	}

	/**
	 * @param treeLevel the treeLevel to set
	 */
	public void setTreeLevel(String treeLevel) {
		this.treeLevel = treeLevel;
	}
	
	/**
	 * @return the children
	 */
	public List<Object> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Object> children) {
		this.children = children;
	}

	/**
	 * add node to chaild
	 * @param node
	 */
	public void addChild(Object node) {
		this.children.add(node);
	}
	
	@JsonIgnore
	public String getTreeName_() {
		if (StringUtils.isNotBlank(this.treeName_)) {
			return this.treeName_;
		}
		List<Field> fieldList = TableInfoHelper.getAllFields(getClass());
		for (Field field : fieldList) {
			if (field.isAnnotationPresent(TreeName.class)) {
				this.treeName_ = (String)ReflectUtil.getFieldValue(getClass(), field.getName());
				break;
			}
		}
		if (StringUtils.isEmpty(this.treeName_)) {
			throw new ServiceException("树状结构需要设置节点名称注解@TreeName");
		}
		return this.treeName_;
	}
	
	/**
	 * 判断是否根节点
	 * @return
	 */
	public boolean isRoot() {
		return ROOT_ID.equals(getParentId());
	}
	
	/**
	 * 根节点编码
	 */
    public static final String ROOT_ID = "0";

}
