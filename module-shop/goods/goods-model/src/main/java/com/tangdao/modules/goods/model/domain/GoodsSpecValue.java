package com.tangdao.modules.goods.model.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

/**
 * <p>
 * 商品规格关联价格表
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Data
@TableName("goods_spec_value")
public class GoodsSpecValue implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @TableId
	protected String id;

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 规格键名
     */
    private String key;

    /**
     * 规格键名中文
     */
    private String keyName;

    /**
     * 价格(供货价格)
     */
    private BigDecimal shopPrice;

    /**
     * 零售价格
     */
    private Double marketPrice;

    /**
     * 库存数量
     */
    private Integer storeCount;

    /**
     * 商品条形码
     */
    private String barCode;

    /**
     * SKU
     */
    private String sku;

    /**
     * 商品缩略图
     */
    private String thumbnail;


}
