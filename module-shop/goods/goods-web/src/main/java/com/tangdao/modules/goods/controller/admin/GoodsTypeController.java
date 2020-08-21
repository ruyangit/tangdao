/**
 *
 */
package com.tangdao.modules.goods.controller.admin;

import java.util.List;
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
import com.tangdao.modules.goods.model.domain.GoodsAttribute;
import com.tangdao.modules.goods.model.domain.GoodsType;
import com.tangdao.modules.goods.model.vo.GoodsTypeVo;
import com.tangdao.modules.goods.service.GoodsAttributeService;
import com.tangdao.modules.goods.service.GoodsTypeService;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年8月6日
 */
@RestController
@RequestMapping(value = { "/admin/goods" })
public class GoodsTypeController extends BaseController {

	@Autowired
	GoodsTypeService goodsTypeService;
	
	@Autowired
	GoodsAttributeService goodsAttributeService;

	@GetMapping("/types")
	public CommonResponse page(Page<GoodsType> page, String typeName) {
		QueryWrapper<GoodsType> queryWrapper = new QueryWrapper<GoodsType>();
		if (StrUtil.isNotBlank(typeName)) {
			queryWrapper.like("type_name", typeName);
		}
		return success(goodsTypeService.page(page, queryWrapper));
	}
	
	@Validate({ @Field(name = "id", rules = { @Rule(message = "主键不能为空") }) })
	@GetMapping("/type-detail")
	public CommonResponse detail(String id) {
		GoodsType goodsType = goodsTypeService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("goodsType", goodsType);
		QueryWrapper<GoodsAttribute> queryWrapper = new QueryWrapper<GoodsAttribute>();
		queryWrapper.eq("type_id", id);
		List<GoodsAttribute> goodsAttributes = goodsAttributeService.list(queryWrapper);
		data.put("goodsAttributes", goodsAttributes);
		return success(data);
	}

	@PostMapping("/types")
	public CommonResponse saveGoodsType(@RequestBody GoodsTypeVo goodsTypeVo) {
		return success(goodsTypeService.saveTypeAndAttr(goodsTypeVo));
	}

	@Validate({ @Field(name = "goodsTypeVo.goodsType.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/type-update")
	public CommonResponse updateGoodsType(@RequestBody GoodsTypeVo goodsTypeVo) {
		return success(goodsTypeService.updateTypeAndAttrById(goodsTypeVo));
	}

	@Validate({ @Field(name = "goodsAttribute.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/attribute-delete")
	public CommonResponse deleteGoodsType(@RequestBody GoodsAttribute goodsAttribute) {
		return success(goodsAttributeService.removeById(goodsAttribute.getId()));
	}
	
	@Validate({ @Field(name = "goodsType.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/type-delete")
	public CommonResponse deleteGoodsType(@RequestBody GoodsType goodsType) {
		return success(goodsTypeService.removeById(goodsType.getId()));
	}
}
