package com.tangdao.module.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tangdao.framework.persistence.DataEntity;

/**
 * <p>
 * 用户组表
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-03-11
 */
@TableName("sys_group")
public class Group extends DataEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户组主键
     */
    @TableId(value = "group_id", type = IdType.ASSIGN_ID)
    private String groupId;

    /**
     * 用户组名称
     */
    private String groupName;

    /**
     * 主体编码
     */
    private String corpId;

    /**
     * 主体名称
     */
    private String corpName;

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

    @Override
    public String toString() {
        return "Group{" +
            "groupId=" + groupId +
            ", groupName=" + groupName +
            ", corpId=" + corpId +
            ", corpName=" + corpName +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
        "}";
    }
}
