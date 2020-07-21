/**
 *
 */
package com.tangdao.model.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tangdao.common.constant.CommonContext;

import cn.hutool.core.util.StrUtil;
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

	@JsonIgnore
	private String pId;

	@JsonIgnore
	private String pIds;

	private String name;

	private String path;

	private String icon;

	private String opened;
	
	@JsonIgnore
	private String isShow;

	private String badge;

	private List<MenuVo> children;

	public static final String TYPE_MENU = "1";
	public static final String TYPE_PERM = "2";
	
	public void addChild(MenuVo menu) {
		this.children.add(menu);
	}
	
	public boolean getOpened() {
		return StrUtil.equals(opened, CommonContext.YES);
	}
	
	public boolean getIsShow() {
		return StrUtil.equals(opened, CommonContext.YES);
	}

}
