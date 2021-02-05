/**
 *
 */
package com.tangdao.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.core.DataEntity;

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
public class Menu extends DataEntity<DictType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@TableId
	private String menuCode;
}
