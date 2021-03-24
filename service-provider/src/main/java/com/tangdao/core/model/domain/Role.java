/**
 *
 */
package com.tangdao.core.model.domain;

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
 * @since 2021年3月8日
 */
@Getter
@Setter
@TableName("sys_role")
public class Role extends DataEntity<Role> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String id;

	private String roleCode;
	
	private String roleName;
	
	private String status;
	
	private String remarks;
	
}
