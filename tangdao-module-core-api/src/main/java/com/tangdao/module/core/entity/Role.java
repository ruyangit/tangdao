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

    /**
     * 编号
     */
    private String domainId;

    /**
     * 权限标识
     */
    private String roleCode;

    /**
     * 角色类型
     */
    private String roleType;
    

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
    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }
    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }
    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "Role{" +
            "roleId=" + roleId +
            ", roleName=" + roleName +
            ", domainId=" + domainId +
            ", roleCode=" + roleCode +
            ", roleType=" + roleType +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
