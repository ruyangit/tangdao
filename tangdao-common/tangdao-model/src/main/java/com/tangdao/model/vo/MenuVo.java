/**
 *
 */
package com.tangdao.model.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月18日
 */
@Getter
@Setter
public class MenuVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String pId;

	private String pIds;

	private String name;

	private String path;

	private List<String> premissions;

	private String icon;

	private Boolean link;

	private Boolean opened;

	private String badge;

	private List<MenuVo> children;

	public static final String TYPE_MENU = "1";
	public static final String TYPE_PERM = "2";
	
	public void addChild(MenuVo menu) {
		this.children.add(menu);
	}

}
