package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 产品品牌
 * </p>
 *
 * @author ruyang
 * @since 2020-08-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_brand")
public class GoodsBrand extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺ID
     */
    private String storeId;

    /**
     * 品牌logo
     */
    private String brandLogo;

    /**
     * 品牌封面
     */
    private String brandCover;

    /**
     * 品牌首字母
     */
    private String brandInitial;

    /**
     * 商品品牌名称
     */
    private String brandName;

    /**
     * 商品品牌摘要
     */
    private String summary;

    /**
     * 品牌图文信息
     */
    private String content;

    /**
     * 商品分类排序
     */
    private BigDecimal sort;

    private String options;

    /**
     * 商品状态
     */
    private String status;

    /**
     * 类别名称
     */
    private String brandClass;

    /**
     * 品牌推荐，0为否，1为是
     */
    private String brandRecommend;

    /**
     * 品牌申请，1为申请中，0为通过，默认为1，申请功能是会员使用
     */
    private String brandApply;

    
    public void setEmpty() {
    	this.setStatus(null);
    	this.setCreated(null);
    }

}
