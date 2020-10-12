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
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.User;
import com.tangdao.model.dto.FieldDTO;
import com.tangdao.modules.goods.model.domain.Goods;
import com.tangdao.modules.goods.model.dto.GoodsDTO;
import com.tangdao.modules.goods.service.GoodsBrandService;
import com.tangdao.modules.goods.service.GoodsImageService;
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

	@Autowired
	GoodsImageService goodsImageService;

	@Validate({ @Field(name = "field.id", rules = { @Rule(message = "id不能为空") }),
			@Field(name = "field.name", rules = { @Rule(message = "name不能为空") }) })
	@PostMapping("/goods-field")
	public CommonResponse field(@RequestBody FieldDTO field) {
		UpdateWrapper<Goods> updateWrapper = new UpdateWrapper<Goods>();
		updateWrapper.set(field.getName(), field.getValue());
		updateWrapper.eq("id", field.getId());
		return success(goodsService.update(updateWrapper));
	}

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
		data.put("goodsImages", this.goodsImageService.listByGoodsId(id));
		return success(data);
	}

	@Validate({ @Field(name = "goodsDTO.goodsName", rules = { @Rule(message = "商品名称不能为空") }) })
	@PostMapping
	public CommonResponse saveGoods(@RequestBody GoodsDTO goodsDTO) {
		this.goodsService.save(goodsDTO);
		return CommonResponse.createCommonResponse().success();
	}

	@Validate({ @Field(name = "goodsDTO.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/update")
	public CommonResponse updateGoods(@RequestBody GoodsDTO goodsDTO) {
		this.goodsService.updateById(goodsDTO);
		return CommonResponse.createCommonResponse().success();
	}

	@Validate({ @Field(name = "goods.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/delete")
	public CommonResponse deleteBrand(@RequestBody Goods goods) {
		return success(goodsService.removeById(goods.getId()));
	}
}
