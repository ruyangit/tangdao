package com.tangdao.module.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangdao.framework.persistence.DataEntity;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@TableName("sys_role")
public class Role extends DataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色编码
     */
    @TableId(value = "role_id", type = IdType.ASSIGN_ID)
    private String roleId;

    /**
     * 角色名称
     */
    private String roleName;

    private String corpId;
    
    private String corpName;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorpName() {
		return corpName;
	}

	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}

}
