package com.tangdao.modules.goods.service;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.goods.mapper.GoodsCategoryMapper;
import com.tangdao.modules.goods.model.domain.GoodsCategory;
import com.tangdao.modules.goods.model.vo.GoodsCategoryVo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 商城商品分类 服务实现类
 * </p>
 *
 * @author ruyang
 * @since 2020-08-19
 */
@Service
public class GoodsCategoryService extends BaseService<GoodsCategoryMapper, GoodsCategory> {

	public List<GoodsCategoryVo> findGoodsCategoryChildList() {
		List<GoodsCategory> sourceList = this.baseMapper.selectList(Wrappers.emptyWrapper());
		Map<String, GoodsCategoryVo> dtoMap = new LinkedHashMap<String, GoodsCategoryVo>();
		for (GoodsCategory goodsCategory : sourceList) {
			GoodsCategoryVo goodsCategoryVo = new GoodsCategoryVo();
			goodsCategoryVo.setChildren(null);
			BeanUtil.copyProperties(goodsCategory, goodsCategoryVo);
			dtoMap.put(goodsCategory.getId(), goodsCategoryVo);
		}
		List<GoodsCategoryVo> targetList = CollUtil.newLinkedList();
		for (Map.Entry<String, GoodsCategoryVo> entry : dtoMap.entrySet()) {
			GoodsCategoryVo item = entry.getValue();
			String tpid = item.getPId();
			if (dtoMap.get(tpid) == null) {
				// 如果是顶层节点，直接添加到结果集合中
				targetList.add(item);
			} else {
				// 如果不是顶层节点，有父节点，然后添加到父节点的子节点中
				GoodsCategoryVo parent = dtoMap.get(tpid);
				if (parent.getChildren() == null) {
					parent.setChildren(new LinkedList<GoodsCategoryVo>());
				}
				parent.addChild(item);
			}
		}
		return targetList;
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean saveOrUpdate(GoodsCategory goodsCategory) {
		if (null != goodsCategory) {

			GoodsCategory parentGoodsCategory = null;
			if (!StringUtils.checkValNull(goodsCategory.getPId())) {
				parentGoodsCategory = super.getById(goodsCategory.getPId());
			}
			if (parentGoodsCategory == null) {
				parentGoodsCategory = new GoodsCategory();
				parentGoodsCategory.setId(MenuVo.ROOT_ID);
				parentGoodsCategory.setPIds(StringUtils.EMPTY);
				goodsCategory.setPId(parentGoodsCategory.getId());
			}
			GoodsCategory oldGoodsCategory = super.getById(goodsCategory.getId());
			
			goodsCategory.setPIds(parentGoodsCategory.getPIds() + parentGoodsCategory.getId() + ",");
			super.saveOrUpdate(goodsCategory);

			List<GoodsCategory> children = this.list(Wrappers.<GoodsCategory>lambdaQuery().like(GoodsCategory::getPIds, goodsCategory.getId()));
			children.stream().filter(e -> e.getPIds() != null && oldGoodsCategory != null).forEach(e -> {
				e.setPIds(e.getPIds().replace(oldGoodsCategory.getPIds(), goodsCategory.getPIds()));
				this.updateById(e);
			});
			return true;
		}
		return false;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public boolean removeChildById(Serializable id) {
		this.removeById(id);
		this.remove(Wrappers.<GoodsCategory>lambdaUpdate().like(GoodsCategory::getPIds, id));
		return true;
	}
}
