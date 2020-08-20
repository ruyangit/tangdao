package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsType;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品类型表 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-08-20
 */
@Mapper
public interface GoodsTypeMapper extends BaseMapper<GoodsType> {

}
