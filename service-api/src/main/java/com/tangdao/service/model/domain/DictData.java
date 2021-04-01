/**
 *
 */
package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.TreeEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@Getter
@Setter
@TableName("sys_dict_data")
public class DictData extends TreeEntity<DictData>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;

	private String dictType;
	
	private String dictKey;
	
	private String dictLabel;
	
	private String dictValue;
	
	private String description;
	
	private String cssStyle;
	
	private String cssClass;

	private String status;
	
	private String remarks;
}
