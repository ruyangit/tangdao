package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsSpecItem;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品的规格选项 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Mapper
public interface GoodsSpecItemMapper extends BaseMapper<GoodsSpecItem> {

}
