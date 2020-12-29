/**
 *
 */
package com.tangdao.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.common.BaseModel;

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
public class Menu extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String menuCode;
}
