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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.DictType;
import com.tangdao.service.model.dto.DictTypeDTO;
import com.tangdao.service.provider.DictTypeService;

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
@RequestMapping("/dictType")
public class DictTypeController extends BaseController {

	@Autowired
	private DictTypeService dictTypeService;

	@LogOpt(logTitle = "根据指定列获取详细数据")
	@GetMapping
	public CommonResponse field(String column, String value) {
		QueryWrapper<DictType> queryWrapper = new QueryWrapper<DictType>();
		queryWrapper.eq(column, value);
		return renderResult(dictTypeService.getOne(queryWrapper));
	}

	@LogOpt(logTitle = "获取列表分页数据")
	@GetMapping("/page")
	public CommonResponse page(Page<DictType> page, DictType dictType) {
		LambdaQueryWrapper<DictType> queryWrapper = Wrappers.<DictType>lambdaQuery();
		if (StrUtil.isNotBlank(dictType.getDictName())) {
			queryWrapper.likeRight(DictType::getDictName, dictType.getDictName());
		}
		if (StrUtil.isNotBlank(dictType.getDictType())) {
			queryWrapper.like(DictType::getDictType, dictType.getDictType());
		}
		return renderResult(dictTypeService.page(page, queryWrapper));
	}

	@LogOpt(logTitle = "新增或更新数据")
	@PostMapping("/save")
	public CommonResponse save(@RequestBody DictTypeDTO dictType) {
		if (!dictTypeService.checkDictTypeExists(dictType.getOldDictType(), dictType.getDictType())) {
			return renderResult(Global.FALSE, "字典'" + dictType.getDictType() + "'保存失败，字典类型已存在");
		}
		dictTypeService.saveOrUpdate(dictType);
		return renderResult(Global.TRUE, "保存成功");
	}

	@LogOpt(logTitle = "根据主键删除数据")
	@PostMapping("/delete")
	public CommonResponse delete(@RequestBody DictTypeDTO dictType) {
		dictTypeService.deleteDictType(dictType);
		return renderResult(Global.TRUE, "删除成功");
	}
}
