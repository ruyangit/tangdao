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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.modules.goods.model.domain.Goods;
import com.tangdao.modules.goods.service.GoodsBrandService;
import com.tangdao.modules.goods.service.GoodsService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 商品管理主表
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年8月6日
 */
@RestController
@RequestMapping(value = { "/admin/goods" })
public class GoodsController extends BaseController {

	@Autowired
	GoodsBrandService goodsBrandService;
	
	@Autowired
	GoodsService goodsService;

	@GetMapping
	public CommonResponse page(Page<Goods> page, String goodsName) {
		QueryWrapper<Goods> queryWrapper = new QueryWrapper<Goods>();
		if (StrUtil.isNotBlank(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		return success(goodsService.page(page, queryWrapper));
	}

	@Validate({ @Field(name = "id", rules = { @Rule(message = "主键不能为空") }) })
	@GetMapping("/detail")
	public CommonResponse detail(String id) {
		Goods goods = goodsService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("goods", goods);
		return success(data);
	}

	@Validate({ @Field(name = "goods.goodsName", rules = { @Rule(message = "商品名称不能为空") }) })
	@PostMapping
	public CommonResponse saveGoods(@RequestBody Goods goods) {
		return success(goodsService.save(goods));
	}

	@Validate({ @Field(name = "goods.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/update")
	public CommonResponse updateGoods(@RequestBody Goods goods) {
		return success(goodsService.updateById(goods));
	}

	@Validate({ @Field(name = "goods.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/delete")
	public CommonResponse deleteBrand(@RequestBody Goods goods) {
		return success(goodsService.removeById(goods.getId()));
	}
}
