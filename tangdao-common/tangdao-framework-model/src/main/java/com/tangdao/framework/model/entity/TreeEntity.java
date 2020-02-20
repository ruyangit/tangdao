/**
 * 
 */
package com.tangdao.framework.model.entity;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tangdao.common.exception.ServiceException;
import com.tangdao.framework.model.TreeName;

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
    private String parentCode;

    /**
     * 所有父级编号
     */
    private String parentCodes;

    /**
     * 本级排序号（升序）
     */
    private BigDecimal treeSort;
    
    /**
     * 节点名称
     */
    @JsonIgnore
	@TableField(exist = false)
	protected String treeName_;

    /**
     * 全节点名
     */
    private String treeNames;

    /**
     * 是否最末级
     */
    private String treeLeaf;

    /**
     * 层次级别
     */
    private String treeLevel;
    
    /**
     * 子节点
     */
    @TableField(exist = false)
	protected List<Object> children;
    
    /**
	 * 根节点编码
	 */
	public static final String ROOT_CODE = "0";

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the parentCodes
	 */
	public String getParentCodes() {
		return parentCodes;
	}

	/**
	 * @param parentCodes the parentCodes to set
	 */
	public void setParentCodes(String parentCodes) {
		this.parentCodes = parentCodes;
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
	 * isRoot eq 0
	 * @return
	 */
	public boolean isRoot() {
		return ROOT_CODE.equals(getParentCode());
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

}
