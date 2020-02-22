package com.tangdao.module.core.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.framework.persistence.TreeEntity;
import com.tangdao.framework.persistence.TreeName;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@TableName("sys_menu")
public class Menu extends TreeEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编码
     */
    @TableId(value = "menu_id", type = IdType.ASSIGN_ID)
    private String menuId;

    /**
     * 菜单名称
     */
    @TreeName
    private String menuName;

    /**
     * 菜单类型（1菜单 2权限 3开发）
     */
    private String menuType;

    /**
     * 链接
     */
    private String menuHref;

    /**
     * 目标
     */
    private String menuTarget;

    /**
     * 图标
     */
    private String menuIcon;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 是否显示（1显示 0隐藏）
     */
    private String isShow;

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }
    public String getMenuHref() {
        return menuHref;
    }

    public void setMenuHref(String menuHref) {
        this.menuHref = menuHref;
    }
    public String getMenuTarget() {
        return menuTarget;
    }

    public void setMenuTarget(String menuTarget) {
        this.menuTarget = menuTarget;
    }
    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    @Override
    public String toString() {
        return "Menu{" +
            "menuId=" + menuId +
            ", menuName=" + menuName +
            ", menuType=" + menuType +
            ", menuHref=" + menuHref +
            ", menuTarget=" + menuTarget +
            ", menuIcon=" + menuIcon +
            ", permission=" + permission +
            ", isShow=" + isShow +
    		", parentId=" + parentId +
    		", parentIds=" + parentIds +
    		", treeSort=" + treeSort +
    		", treeNames=" + treeNames +
    		", status=" + status +
    		", createBy=" + createBy +
    		", createTime=" + createTime +
    		", updateBy=" + updateBy +
    		", updateTime=" + updateTime +
    		", remarks=" + remarks +
    		", treeLeaf=" + treeLeaf +
    		", treeLevel=" + treeLevel +
        "}";
    }
}
