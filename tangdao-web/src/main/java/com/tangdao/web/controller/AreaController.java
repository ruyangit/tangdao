/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.service.provider.AreaService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年4月9日
 */
@RestController
@RequestMapping("/api")
public class AreaController extends BaseController {

	@Autowired
	private AreaService areaService;

//	@LogOpt(logTitle = "获取节点数据")
	@GetMapping("/tree")
	public CommonResponse tree() {
		return renderResult(areaService.getTreeList(null));
	}
}
