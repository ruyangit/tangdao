/**
 *
 */
package com.tangdao.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Getter
@Setter
@TableName("sys_dict_data")
public class DictData extends DataEntity<DictData> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String dictCode;
}
