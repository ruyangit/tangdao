package com.tangdao.modules.goods.model.domain;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 商品关联的属性表
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Data
@TableName("goods_attribute_value")
public class GoodsAttributeValue implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId
	protected String id;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 属性id
     */
    private String attrId;

    /**
     * 属性值
     */
    private String attrValue;


}
