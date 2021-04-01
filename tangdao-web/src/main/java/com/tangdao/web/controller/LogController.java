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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.model.SysLog;
import com.tangdao.core.service.ILogService;
import com.tangdao.core.web.BaseController;

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
@RequestMapping("/log")
public class LogController extends BaseController{

	@Autowired
	private ILogService logService;
	
	@GetMapping("/page")
	public CommonResponse page(Page<SysLog> page, SysLog log) {
		LambdaQueryWrapper<SysLog> queryWrapper = Wrappers.<SysLog>lambdaQuery();
		if (StrUtil.isNotBlank(log.getLogTitle())) {
			queryWrapper.likeRight(SysLog::getLogTitle, log.getLogTitle());
		}
		if (StrUtil.isNotBlank(log.getLogType())) {
			queryWrapper.eq(SysLog::getLogType, log.getLogType());
		}
		queryWrapper.orderByDesc(SysLog::getCreateDate);
		return renderResult(logService.page(page, queryWrapper));
	}
}
