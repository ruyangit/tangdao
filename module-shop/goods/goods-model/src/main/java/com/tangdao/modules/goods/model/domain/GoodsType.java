package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品类型表
 * </p>
 *
 * @author ruyang
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_type")
public class GoodsType extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 类型名称
     */
    private String typeName;

    private BigDecimal sort;

    private String status;


}
