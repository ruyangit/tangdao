/**
 *
 */
package com.tangdao.service.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
@TableName("sys_dict_type")
public class DictType {

	@TableId
	private String id;
}
