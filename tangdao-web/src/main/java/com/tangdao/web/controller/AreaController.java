/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.annotation.LogOpt;
import com.tangdao.core.config.Global;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.model.domain.Area;
import com.tangdao.service.model.dto.AreaDTO;
import com.tangdao.service.provider.AreaService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月9日
 */
@RestController
@RequestMapping("/area")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

	@LogOpt(logTitle = "获取节点数据")
	@GetMapping("/treeData")
	public CommonResponse treeData() {
		LambdaQueryWrapper<Area> quearyWrapper = Wrappers.<Area>lambdaQuery().eq(Area::getStatus, Area.NORMAL)
				.orderByAsc(Area::getTreeSort);
		return renderResult(areaService.getTreeList(quearyWrapper));
	}

	@LogOpt(logTitle = "获取节点数据")
	@GetMapping("/listData")
	public CommonResponse listData(AreaDTO areaDTO) {
		LambdaQueryWrapper<Area> quearyWrapper = Wrappers.<Area>lambdaQuery().eq(Area::getStatus, Area.NORMAL)
				.orderByAsc(Area::getTreeSort);
		if (StrUtil.isEmpty(areaDTO.getPid())) {
			areaDTO.setPid(Global.ROOT_ID);
		}
		quearyWrapper.eq(Area::getPid, areaDTO.getPid());
		return renderResult(areaService.list(quearyWrapper));
	}
}
