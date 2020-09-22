package com.tangdao.modules.goods.model.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品相册
 * </p>
 *
 * @author ruyang
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_album")
public class GoodsAlbum extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 上级分类编号
     */
    private String pId;

    private String pIds;

    /**
     * 商户ID
     */
    private String shopId;

    /**
     * 相册名称
     */
    private String albumName;

    /**
     * 相册封面
     */
    private String albumCover;

    /**
     * 文件数量
     */
    private BigDecimal fileCount;

    /**
     * 商品分类排序
     */
    private BigDecimal sort;


}
