package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsAttribute;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品的属性表(独立) Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-08-20
 */
@Mapper
public interface GoodsAttributeMapper extends BaseMapper<GoodsAttribute> {

}
