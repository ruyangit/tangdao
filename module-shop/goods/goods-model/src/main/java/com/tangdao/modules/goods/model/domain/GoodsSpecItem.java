package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品的规格选项
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_spec_item")
public class GoodsSpecItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 规格id
     */
    private String specId;

    /**
     * 规格值
     */
    private String itemValue;

    /**
     * 规格排序
     */
    private BigDecimal sort;

    private String status;


}
