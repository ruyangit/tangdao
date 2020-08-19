package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsCategory;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商城商品分类 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-08-19
 */
@Mapper
public interface GoodsCategoryMapper extends BaseMapper<GoodsCategory> {

}
