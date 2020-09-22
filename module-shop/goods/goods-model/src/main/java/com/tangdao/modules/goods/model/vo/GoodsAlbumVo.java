/**
 *
 */
package com.tangdao.modules.goods.model.vo;

import java.util.List;

import com.tangdao.modules.goods.model.domain.GoodsAlbum;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年9月21日
 */
@Getter
@Setter
public class GoodsAlbumVo extends GoodsAlbum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String ROOT_ID = "0";
	
	private List<GoodsAlbumVo> children;

	public void addChild(GoodsAlbumVo entity) {
		this.children.add(entity);
	}
	
	public String getName() {
		return this.getAlbumName();
	}
}
