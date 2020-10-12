package com.tangdao.modules.goods.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.modules.goods.mapper.GoodsImageMapper;
import com.tangdao.modules.goods.model.domain.GoodsImage;

/**
 * <p>
 * TODO 商品图片表 服务实现类
 * </p>
 *
 * @author ruyang
 * @since 2020-10-12
 */
@Service
public class GoodsImageService extends BaseService<GoodsImageMapper, GoodsImage> {
	
	public List<GoodsImage> listByGoodsId(String goodsId){
		return super.list(Wrappers.<GoodsImage>lambdaQuery().eq(GoodsImage::getGoodsId, goodsId));
	}

	public boolean removeByGoodsId(String goodsId) {
		return super.remove(Wrappers.<GoodsImage>lambdaQuery().eq(GoodsImage::getGoodsId, goodsId));
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void saveBatch(List<String> goodsImages, String goodsId) {
		if (goodsImages != null) {
			List<GoodsImage> goodsImageList = new ArrayList<GoodsImage>();
			for (int i = 0; i < goodsImages.size(); i++) {
				GoodsImage goodsImage = new GoodsImage();
				goodsImage.setGoodsId(goodsId);
				goodsImage.setSrc(goodsImages.get(i));
				goodsImage.setSort(new BigDecimal(i));
				goodsImageList.add(goodsImage);
			}
			super.saveBatch(goodsImageList);
		}
	}
}
