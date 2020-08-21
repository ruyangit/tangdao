/**
 *
 */
package com.tangdao.modules.goods.model.vo;

import java.io.Serializable;
import java.util.List;

import com.tangdao.modules.goods.model.domain.GoodsAttribute;
import com.tangdao.modules.goods.model.domain.GoodsType;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年8月21日
 */
@Getter
@Setter
public class GoodsTypeVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GoodsType goodsType;
	
	public List<GoodsAttribute> goodsAttributes; 
}
