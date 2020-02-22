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
 * @since 2020-02-22
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
     * 数据范围设置（0未设置  1全部数据 2自定义数据）
     */
    private String dataScope;

    /**
     * 编号
     */
    private String tenantId;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否系统
     */
    private String isSys;

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
    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    public String getIsSys() {
        return isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    @Override
    public String toString() {
        return "Role{" +
            "roleId=" + roleId +
            ", roleName=" + roleName +
            ", dataScope=" + dataScope +
            ", tenantId=" + tenantId +
            ", permission=" + permission +
            ", isSys=" + isSys +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
