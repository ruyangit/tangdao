/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.DictData;
import com.tangdao.service.provider.DictDataService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月1日
 */
@RestController
@RequestMapping("/dictData")
public class DictDataController extends BaseController {

	@Autowired
	private DictDataService dictDataService;

	@LogOpt(logTitle = "获取列表分页数据")
	@GetMapping("/page")
	public CommonResponse page(Page<DictData> page, DictData dictData) {
		LambdaQueryWrapper<DictData> queryWrapper = Wrappers.<DictData>lambdaQuery();
		if (StrUtil.isNotBlank(dictData.getDictLabel())) {
			queryWrapper.likeRight(DictData::getDictLabel, dictData.getDictLabel());
		}
		if (StrUtil.isNotBlank(dictData.getDictType())) {
			queryWrapper.eq(DictData::getDictType, dictData.getDictType());
		}
		if (StrUtil.isNotBlank(dictData.getStatus())) {
			queryWrapper.eq(DictData::getStatus, dictData.getStatus());
		}
		return renderResult(dictDataService.page(page, queryWrapper));
	}
	
	@LogOpt(logTitle = "新增或更新数据")
	@PostMapping("/save")
	public CommonResponse save(@RequestBody DictData dictData) {
		dictDataService.saveOrUpdate(dictData);
		return renderResult(Global.TRUE, "保存成功");
	}

	@LogOpt(logTitle = "根据主键删除数据")
	@PostMapping("/delete")
	public CommonResponse delete(@RequestBody DictData dictData) {
		dictDataService.removeById(dictData);
		return renderResult(Global.TRUE, "删除成功");
	}
}
