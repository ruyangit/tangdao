package com.tangdao.modules.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tangdao.core.service.BaseService;
import com.tangdao.modules.goods.mapper.GoodsTypeMapper;
import com.tangdao.modules.goods.model.domain.GoodsType;
import com.tangdao.modules.goods.model.vo.GoodsTypeVo;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 商品类型表 服务实现类
 * </p>
 *
 * @author ruyang
 * @since 2020-08-20
 */
@Service
public class GoodsTypeService extends BaseService<GoodsTypeMapper, GoodsType> {
	
	@Autowired
	private GoodsAttributeService goodsAttributeService;

	@Transactional(rollbackFor = Exception.class)
	public boolean saveTypeAndAttr(GoodsTypeVo goodsTypeVo) {
		GoodsType goodsType = goodsTypeVo.getGoodsType();
		this.save(goodsType);
		if (CollUtil.isNotEmpty(goodsTypeVo.getGoodsAttributes())) {
			goodsTypeVo.getGoodsAttributes().stream().forEach(goodsAttribute -> {
				goodsAttribute.setTypeId(goodsType.getId());
				goodsAttributeService.save(goodsAttribute);
			});
		}
		return true;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean updateTypeAndAttrById(GoodsTypeVo goodsTypeVo) {
		GoodsType goodsType = goodsTypeVo.getGoodsType();
		this.updateById(goodsType);
		if (CollUtil.isNotEmpty(goodsTypeVo.getGoodsAttributes())) {
			goodsTypeVo.getGoodsAttributes().stream().forEach(goodsAttribute -> {
				goodsAttribute.setTypeId(goodsType.getId());
				if(StrUtil.isEmpty(goodsAttribute.getId()) || goodsAttributeService.getById(goodsAttribute.getId()) == null) {
					goodsAttributeService.save(goodsAttribute);
				}else {
					goodsAttributeService.updateById(goodsAttribute);
				}
			});
		}
		return true;
	}
}
