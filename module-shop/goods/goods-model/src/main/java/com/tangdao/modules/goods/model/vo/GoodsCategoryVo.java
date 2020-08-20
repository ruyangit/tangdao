/**
 *
 */
package com.tangdao.modules.goods.model.vo;

import java.util.List;

import com.tangdao.modules.goods.model.domain.GoodsCategory;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年8月20日
 */
@Getter
@Setter
public class GoodsCategoryVo extends GoodsCategory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ROOT_ID = "0";
	
	private List<GoodsCategoryVo> children;

	public void addChild(GoodsCategoryVo entity) {
		this.children.add(entity);
	}
	
	public String getName() {
		return this.getCategoryName();
	}
}
