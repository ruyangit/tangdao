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
import com.tangdao.service.model.dto.DictDataDTO;
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
		dictDataService.deleteyIdAndChildren(dictData);
		return renderResult(Global.TRUE, "删除成功");
	}

	@GetMapping("/treeData")
	public CommonResponse tree(DictData dictData, String excludeCode) {
		LambdaQueryWrapper<DictData> quearyWrapper = Wrappers.<DictData>lambdaQuery().eq(DictData::getStatus,
				DictData.NORMAL);
		quearyWrapper.eq(DictData::getDictType, dictData.getDictType());
		if (StrUtil.isNotBlank(dictData.getPid())) {
			quearyWrapper.eq(DictData::getPid, dictData.getPid());
		}
		if (StrUtil.isNotBlank(excludeCode)) {
			quearyWrapper.ne(DictData::getId, excludeCode);
			quearyWrapper.notLike(DictData::getPids, "," + excludeCode + ",");
		}
		quearyWrapper.orderByAsc(DictData::getTreeSort);
		return renderResult(dictDataService.getTreeList(quearyWrapper));
	}

	@GetMapping("/listData")
	public CommonResponse listData(DictDataDTO dictDataDTO) {
		LambdaQueryWrapper<DictData> quearyWrapper = Wrappers.<DictData>lambdaQuery().orderByAsc(DictData::getTreeSort);
		if (StrUtil.isEmpty(dictDataDTO.getPid())) {
			dictDataDTO.setPid(Global.ROOT_ID);
		}
		quearyWrapper.eq(DictData::getDictType, dictDataDTO.getDictType());
		quearyWrapper.eq(DictData::getPid, dictDataDTO.getPid());
		return renderResult(dictDataService.list(quearyWrapper));
	}
}
