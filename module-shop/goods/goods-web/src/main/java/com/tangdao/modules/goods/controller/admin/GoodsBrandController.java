/**
 *
 */
package com.tangdao.modules.goods.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.modules.goods.model.domain.GoodsBrand;
import com.tangdao.modules.goods.service.GoodsBrandService;

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
@RequestMapping(value = { "/goods/brands" })
public class GoodsBrandController extends BaseController {

	@Autowired
	GoodsBrandService goodsBrandService;

	@GetMapping
	public CommonResponse page(Page<GoodsBrand> page, String brandName) {
		QueryWrapper<GoodsBrand> queryWrapper = new QueryWrapper<GoodsBrand>();
		if (StrUtil.isNotBlank(brandName)) {
			queryWrapper.like("brand_name", brandName);
		}
		return success(goodsBrandService.page(page, queryWrapper));
	}
}
