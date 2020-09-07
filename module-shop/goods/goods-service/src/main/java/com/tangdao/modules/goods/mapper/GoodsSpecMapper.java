package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsSpec;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品的规格(独立) Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Mapper
public interface GoodsSpecMapper extends BaseMapper<GoodsSpec> {

}
