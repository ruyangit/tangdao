/**
 *
 */
package com.tangdao.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.tangdao.service.model.domain.Config;
import com.tangdao.service.model.dto.ConfigDTO;
import com.tangdao.service.provider.ConfigService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 配置管理
 * </p>
 *
 * @author ruyang
 * @since 2021年3月30日
 */
@RestController
@RequestMapping("/config")
public class ConfigController extends BaseController {

	@Autowired
	private ConfigService configService;

	@LogOpt(logTitle = "根据指定列获取详细数据")
	@GetMapping
	public CommonResponse field(String column, Object value) {
		QueryWrapper<Config> queryWrapper = new QueryWrapper<Config>();
		queryWrapper.eq(column, value);
		return renderResult(configService.getOne(queryWrapper));
	}

	@PreAuthorize(value="hasAuthority('sys:config:view')")
	@LogOpt(logTitle = "获取列表分页数据")
	@GetMapping("/page")
	public CommonResponse page(Page<Config> page, Config config) {
		LambdaQueryWrapper<Config> queryWrapper = Wrappers.<Config>lambdaQuery();
		if (StrUtil.isNotBlank(config.getConfigName())) {
			queryWrapper.likeRight(Config::getConfigName, config.getConfigName());
		}
		if (StrUtil.isNotBlank(config.getConfigKey())) {
			queryWrapper.like(Config::getConfigKey, config.getConfigKey());
		}
		return renderResult(configService.page(page, queryWrapper));
	}

	@LogOpt(logTitle = "新增或更新数据")
	@PostMapping("/save")
	public CommonResponse save(@RequestBody ConfigDTO config) {
		if (!configService.checkConfigKeyExists(config.getOldConfigKey(), config.getConfigKey())) {
			return renderResult(Global.FALSE, "配置'" + config.getConfigKey() + "'失败，配置键已存在");
		}
		configService.saveOrUpdate(config);
		return renderResult(Global.TRUE, "保存成功");
	}
	
	@LogOpt(logTitle = "根据主键删除数据")
	@PostMapping("/delete")
	public CommonResponse delete(@RequestBody ConfigDTO config) {
		configService.removeById(config);
		return renderResult(Global.TRUE, "删除成功");
	}
}
