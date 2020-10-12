/**
 *
 */
package com.tangdao.modules.goods.model.dto;

import java.util.List;

import com.tangdao.modules.goods.model.domain.Goods;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 商品管理
 * </p>
 *
 * @author ruyang
 * @since 2020年10月12日
 */
@Getter
@Setter
public class GoodsDTO extends Goods {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> goodsImages;
}
