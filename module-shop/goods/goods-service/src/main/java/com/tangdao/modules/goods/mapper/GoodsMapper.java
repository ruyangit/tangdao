package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.Goods;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品主表 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-08-21
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

}
