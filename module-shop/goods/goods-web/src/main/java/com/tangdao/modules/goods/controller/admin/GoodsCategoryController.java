/**
 *
 */
package com.tangdao.modules.goods.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.modules.goods.model.domain.GoodsCategory;
import com.tangdao.modules.goods.service.GoodsCategoryService;

import cn.hutool.core.map.MapUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年8月16日
 */
@RestController
@RequestMapping(value = { "/admin/goods" })
public class GoodsCategoryController extends BaseController {

	@Autowired
	GoodsCategoryService goodsCategoryService;

	@GetMapping("/category-tree")
	public CommonResponse tree() {
		return success(goodsCategoryService.findGoodsCategoryChildList());
	}

	@Validate({ @Field(name = "id", rules = { @Rule(message = "主键不能为空") }) })
	@GetMapping("/category-detail")
	public CommonResponse detail(String id) {
		GoodsCategory goodsCategory = goodsCategoryService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("goodsCategory", goodsCategory);
		return success(data);
	}

	@Validate({ @Field(name = "goodsCategory.categoryName", rules = { @Rule(message = "分类名不能为空") }) })
	@PostMapping("/category")
	public CommonResponse saveGoodsBrand(@RequestBody GoodsCategory goodsCategory) {
		return success(goodsCategoryService.saveOrUpdate(goodsCategory));
	}

	@Validate({ @Field(name = "goodsCategory.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/category-delete")
	public CommonResponse deleteGoodsBrand(@RequestBody GoodsCategory goodsCategory) {
		return success(goodsCategoryService.removeChildById(goodsCategory.getId()));
	}
}
