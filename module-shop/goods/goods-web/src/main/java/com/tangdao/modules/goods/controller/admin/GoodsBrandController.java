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
import com.tangdao.modules.goods.model.domain.GoodsBrand;
import com.tangdao.modules.goods.service.GoodsBrandService;

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
public class GoodsBrandController extends BaseController {

	@Autowired
	GoodsBrandService goodsBrandService;

	@GetMapping("/brands")
	public CommonResponse page(Page<GoodsBrand> page, String brandName) {
		QueryWrapper<GoodsBrand> queryWrapper = new QueryWrapper<GoodsBrand>();
		if (StrUtil.isNotBlank(brandName)) {
			queryWrapper.like("brand_name", brandName);
		}
		return success(goodsBrandService.page(page, queryWrapper));
	}

	@Validate({ @Field(name = "id", rules = { @Rule(message = "主键不能为空") }) })
	@GetMapping("/brand-detail")
	public CommonResponse detail(String id) {
		GoodsBrand goodsBrand = goodsBrandService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("goodsBrand", goodsBrand);
		return success(data);
	}

	@Validate({ @Field(name = "goodsBrand.brandName", rules = { @Rule(message = "角色名不能为空") }) })
	@PostMapping("/brands")
	public CommonResponse saveGoodsBrand(@RequestBody GoodsBrand goodsBrand) {
		return success(goodsBrandService.save(goodsBrand));
	}

	@Validate({ @Field(name = "goodsBrand.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/brand-update")
	public CommonResponse updateRole(@RequestBody GoodsBrand goodsBrand) {
		goodsBrand.setEmpty();
		return success(goodsBrandService.updateById(goodsBrand));
	}

	@Validate({ @Field(name = "goodsBrand.id", rules = { @Rule(message = "主键不能为空") }) })
	@PostMapping("/brand-delete")
	public CommonResponse deleteGoodsBrand(@RequestBody GoodsBrand goodsBrand) {
		return success(goodsBrandService.removeById(goodsBrand.getId()));
	}
}
