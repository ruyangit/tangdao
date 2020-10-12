package com.tangdao.modules.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tangdao.core.service.BaseService;
import com.tangdao.modules.goods.mapper.GoodsMapper;
import com.tangdao.modules.goods.model.domain.Goods;
import com.tangdao.modules.goods.model.dto.GoodsDTO;

import cn.hutool.core.bean.BeanUtil;

/**
 * <p>
 * TODO 商品主表 服务实现类
 * </p>
 *
 * @author ruyang
 * @since 2020-08-21
 */
@Service
public class GoodsService extends BaseService<GoodsMapper, Goods> {

	@Autowired
	private GoodsImageService goodsImageService;

	@Transactional(rollbackFor = Exception.class)
	public void save(GoodsDTO goodsDTO) {
		Goods goods = new Goods();
		BeanUtil.copyProperties(goodsDTO, goods);
		super.save(goods);
		this.goodsImageService.saveBatch(goodsDTO.getGoodsImages(), goods.getId());
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void updateById(GoodsDTO goodsDTO) {
		Goods goods = new Goods();
		BeanUtil.copyProperties(goodsDTO, goods);
		super.updateById(goods);
		this.goodsImageService.removeByGoodsId(goods.getId());
		this.goodsImageService.saveBatch(goodsDTO.getGoodsImages(), goods.getId());
	}
}
