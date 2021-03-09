/**
 *
 */
package com.tangdao.core.model.domain;

import java.util.List;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.TreeEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 菜单
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Getter
@Setter
@TableName("sys_menu")
public class Menu extends TreeEntity<Menu> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String menuCode;
	
	/**
	 * 菜单名称
	 */
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
	 * 菜单权重
	 */
	private Integer weight;

	/**
	 * 是否显示（1显示 0隐藏）
	 */
	private String isShow;
	
	/**
	 * 状态
	 */
	@TableField(fill = FieldFill.INSERT)
	private String status;

	// 超级管理员访问的最低权重
	public static Integer SUPER_ADMIN_GET_MENU_MIN_WEIGHT = 40;

	// 默认权重
	public static Integer WEIGHT_DEFAULT = 20;
	
	// 管理员权重
	public static Integer WEIGHT_DEFAULT_ADMIN = 60;
	
	// 超管权重
	public static Integer WEIGHT_SUPER_ADMIN = 80;

	// 菜单
	public static final String TYPE_MENU = "1";
	
	// 权限
	public static final String TYPE_PERM = "2";

	// 默认的角色列表
	@TableField(exist = false)
	private List<String> defaultRoleCodes;

	// 角色代码
	@TableField(exist = false)
	private String roleCode;

	// 用户代码
	@TableField(exist = false)
	private String userCode;

}
