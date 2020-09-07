package com.tangdao.modules.goods.mapper;

import com.tangdao.modules.goods.model.domain.GoodsAttributeValue;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 商品关联的属性表 Mapper 接口
 * </p>
 *
 * @author ruyang
 * @since 2020-09-07
 */
@Mapper
public interface GoodsAttributeValueMapper extends BaseMapper<GoodsAttributeValue> {

}
