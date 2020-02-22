package com.tangdao.module.core.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangdao.framework.persistence.DataEntity;

/**
 * <p>
 * 租户表
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@TableName("sys_tenant")
public class Tenant extends DataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "tenant_id", type = IdType.ASSIGN_ID)
    private String tenantId;

    private String tenantName;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Override
    public String toString() {
        return "Tenant{" +
            "tenantId=" + tenantId +
            ", tenantName=" + tenantName +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
