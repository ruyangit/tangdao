package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品主表
 * </p>
 *
 * @author ruyang
 * @since 2020-08-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods")
public class Goods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    private String categoryId;

    /**
     * 扩展分类id
     */
    private String extendCategoryId;

    /**
     * 商品编号
     */
    private String goodsSn;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 点击数
     */
    private BigDecimal clickCount;

    /**
     * 品牌id
     */
    private String brandId;

    /**
     * 库存数量
     */
    private BigDecimal storeCount;

    /**
     * 商品评论数
     */
    private BigDecimal commentCount;

    /**
     * 商品重量克为单位
     */
    private BigDecimal weight;

    /**
     * 商品体积。单位立方米
     */
    private BigDecimal volume;

    /**
     * 市场价(零售价)
     */
    private BigDecimal marketPrice;

    /**
     * 本店价(供货价)
     */
    private BigDecimal shopPrice;

    /**
     * 商品成本价
     */
    private BigDecimal costPrice;

    /**
     * 价格阶梯
     */
    private String priceLadder;

    /**
     * 商品关键词
     */
    private String keywords;

    /**
     * 商品简单描述
     */
    private String goodsSummary;

    /**
     * 商品详细描述
     */
    private String goodsContent;

    /**
     * 手机端商品详情
     */
    private String mobileContent;

    /**
     * 商品上传原始图(主图)
     */
    private String goodsLogo;

    /**
     * 是否为虚拟商品 1是，0否
     */
    private String isVirtual;

    /**
     * 虚拟商品有效期
     */
    private BigDecimal virtualIndate;

    /**
     * 虚拟商品购买上限
     */
    private BigDecimal virtualLimit;

    /**
     * 是否允许过期退款， 1是，0否
     */
    private String virtualRefund;

    /**
     * 是否上架 0-未上架 1-销售中
     */
    private String isOnSale;

    /**
     * 是否包邮0否1是
     */
    private String isFreeShipping;

    /**
     * 商品上架时间
     */
    private LocalDateTime onTime;

    /**
     * 商品排序
     */
    private BigDecimal sort;

    /**
     * 是否推荐0-未推荐 1-已推荐
     */
    private String isRecommend;

    /**
     * 是否新品0-不是 1-是
     */
    private String isNew;

    /**
     * 是否热卖
     */
    private String isHot;

    /**
     * 更新时间
     */
    private LocalDateTime modified;

    /**
     * 商品所属类型id，取值表goods_type的id
     */
    private String goodsType;

    /**
     * 商品规格类型，取值表goods_type的id
     */
    private String specType;

    /**
     * 购买商品赠送积分
     */
    private BigDecimal giveIntegral;

    /**
     * 积分兑换：0不参与积分兑换，积分和现金的兑换比例见后台配置
     */
    private String exchangeIntegral;

    /**
     * 供货商ID
     */
    private String suppliersId;

    /**
     * 商品销量
     */
    private BigDecimal salesSum;

    /**
     * 0默认1抢购2团购3优惠促销4预售5虚拟(5其实没用)6拼团
     */
    private String promType;

    /**
     * 优惠活动id
     */
    private String promId;

    /**
     * 佣金用于分销分成
     */
    private BigDecimal commission;

    /**
     * SPU
     */
    private String spu;

    /**
     * SKU
     */
    private String sku;

    /**
     * 运费模板ID
     */
    private String templateId;

    /**
     * 视频
     */
    private String goodsVideo;

    private String shopId;


}
