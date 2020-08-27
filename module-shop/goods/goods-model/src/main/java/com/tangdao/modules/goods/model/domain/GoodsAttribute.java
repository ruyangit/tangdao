package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品的属性表(独立)
 * </p>
 *
 * @author ruyang
 * @since 2020-08-20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_attribute")
public class GoodsAttribute extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 属性名称
     */
    private String attrName;

    /**
     * 属性分类id(商品类型id)
     */
    private String typeId;

    /**
     * 0唯一属性 1单选属性 2复选属性
     */
    private String attrType;

    /**
     *  0 手工录入 1从列表中选择 2多行文本框
     */
    private String attrInputType;

    /**
     * 可选值列表
     */
    private String attrValues;

    /**
     * 属性排序
     */
    private BigDecimal sort;

    private String status;


}
