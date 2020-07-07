/**
 *
 */
package com.tangdao.model.domain;

import java.util.Date;

import com.tangdao.model.base.BaseEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月17日
 */
@Getter
@Setter
public class Menu extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String menuName;

	private String pId;
	
	private String pIds;

	private String menuPath;

	private String permission;

	private String menuType;

	private Boolean isShow;

	private String icon;

	private Boolean opened;

	private Integer sort;

	private String badge;

	private String remark;

	private String status;

	private Date modified;
}
