package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.annotation.TableTree;
import com.tangdao.core.annotation.TreeName;
import com.tangdao.core.model.TreeEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * 行政区划Entity
 * 
 * @author ruyang
 * @version 2019-09-27
 */
@Getter
@Setter
@TableName("sys_area")
@TableTree(primaryKey = "id", treeNameKey = "areaName")
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id; // 区域编码

	@TreeName
	private String areaName;

	private String areaType;

	private String remarks;
	
}