/**
 *
 */
package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.model.DataEntity;

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
@TableName("sys_dict_type")
public class DictType extends DataEntity<DictType>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@TableId
	private String id;
	
	private String dictName;
	
	private String dictType;
	
	private String status;
	
	private String remarks;
}
