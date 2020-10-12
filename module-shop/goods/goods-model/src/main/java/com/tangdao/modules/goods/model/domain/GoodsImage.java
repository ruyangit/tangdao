package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品图片表
 * </p>
 *
 * @author ruyang
 * @since 2020-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_image")
public class GoodsImage extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    private String goodsId;

    private String src;

    /**
     * 排序
     */
    private BigDecimal sort;


}
