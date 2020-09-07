package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品的规格(独立)
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_spec")
public class GoodsSpec extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 规格名称
     */
    private String specName;

    /**
     * 属性分类id(商品类型id)
     */
    private String typeId;

    /**
     * 0文字，1颜色，3图片
     */
    private String specType;

    /**
     * 规格排序
     */
    private BigDecimal sort;

    private String status;


}
