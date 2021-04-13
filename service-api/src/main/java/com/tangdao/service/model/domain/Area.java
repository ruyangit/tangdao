package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.annotation.Column;
import com.tangdao.core.annotation.Table;
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
@Table(
		columns = { 
		@Column(attrName = "id", isPK = true),
		@Column(attrName = "areaName", isTreeName = true) })
@TableName("sys_area")
public class Area extends TreeEntity<Area> {

	private static final long serialVersionUID = 1L;

	@TableId
	private String id; // 区域编码

	private String areaName;

	private String areaType;
	
	private String status;

	private String remarks;

}