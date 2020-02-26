package com.tangdao.module.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangdao.framework.persistence.DataEntity;

/**
 * <p>
 * 断言表
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@TableName("sys_assertion")
public class Assertion extends DataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "assertion_id", type = IdType.ASSIGN_ID)
    private String assertionId;

    private String policyId;

    private String role;

    private String resource;

    private String action;

    /**
     * 效力（allow允许 deny拒绝）
     */
    private String effect;

    public String getAssertionId() {
        return assertionId;
    }

    public void setAssertionId(String assertionId) {
        this.assertionId = assertionId;
    }
    public String getPolicyId() {
        return policyId;
    }

    public void setPolicyId(String policyId) {
        this.policyId = policyId;
    }
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    @Override
    public String toString() {
        return "Assertion{" +
            "assertionId=" + assertionId +
            ", policyId=" + policyId +
            ", role=" + role +
            ", resource=" + resource +
            ", action=" + action +
            ", effect=" + effect +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
