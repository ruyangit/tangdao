package com.tangdao.modules.goods.model.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.tangdao.model.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品相册文件
 * </p>
 *
 * @author ruyang
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("goods_album_file")
public class GoodsAlbumFile extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 文件编号
     */
    private String fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件分类（image、media、file）
     */
    private String fileType;

    /**
     * 商户ID
     */
    private String shopId;

    private String albumId;

}
