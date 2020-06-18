/**
 *
 */
package com.tangdao.model.domain;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import com.tangdao.common.constant.CommonContext;
import com.tangdao.model.base.BaseEntity;

import cn.hutool.core.util.StrUtil;
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

	private String name;

	private String pId;
	
	private String pIds;

	private String path;

	private String premission;

	private String menuType;

	private String isShow;

	private Integer sort;

	private String icon;

	private String link;

	private String opened;

	private String badge;

	private String remark;

	private String status;

	private Date modified;

	@TableField(exist = false)
	private List<Menu> children;

	public void addChild(Menu menu) {
		this.children.add(menu);
	}

	public boolean show() {
		return StrUtil.equals(CommonContext.YES, this.isShow);
	}
}
