package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商城商品分类
 * </p>
 *
 * @author ruyang
 * @since 2020-08-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_category")
public class GoodsCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上级分类编号
     */
    private String pId;

    private String pIds;

    /**
     * 品牌ID
     */
    private String brandId;

    /**
     * 商品分类名称
     */
    private String categoryName;

    /**
     * 商品分类封面
     */
    private String categoryCover;

    /**
     * 缩略图url
     */
    private String thumbnail;

    private String summary;

    private String content;

    /**
     * 商品分类排序
     */
    private BigDecimal sort;

    private String status;

    public void setEmpty() {
    	this.setStatus(null);
    	this.setCreated(null);
    }
}
