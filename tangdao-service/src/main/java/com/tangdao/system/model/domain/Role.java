/**
 *
 */
package com.tangdao.system.model.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 角色
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Getter
@Setter
@TableName("sys_role")
public class Role extends DataEntity<DictType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String roleCode;
	
	private String roleName;
	
	private String roleType;
	
	private Integer roleSort;
	
	private String isInner;
	
	private String userType;
	
	private String dataScope;
	
	private String remarks;
	
	private String status;
	
	private String tenantCode;
	
	private String tenantName;
	
	// 未设置
	public static final String DATA_SCOPE_NONE = "0";
	
	// 全部数据
	public static final String DATA_SCOPE_ALL = "1";
	
	// 自定义数据
	public static final String DATA_SCOPE_CUSTOM = "2";
	
	@TableField(exist = false)
	private String userCode;
	
	@TableField(exist = false)
	private String oldRoleName;
	
	@TableField(exist = false)
	private List<String> userCodes;
	
	@TableField(exist = false)
	private List<String> menuCodes;
}
