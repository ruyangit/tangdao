package com.tangdao.module.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangdao.framework.persistence.DataEntity;

/**
 * <p>
 * 用户组
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@TableName("sys_group")
public class Group extends DataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "group_id", type = IdType.ASSIGN_ID)
    private String groupId;

    private String groupName;

    /**
     * 编号
     */
    private String tenantId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Group{" +
            "groupId=" + groupId +
            ", groupName=" + groupName +
            ", tenantId=" + tenantId +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
