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

	@GetMapping("/get")
	public CommonResponse get(String configKey) {
		return renderResult(configService.getByConfigKey(configKey));
	}

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

	@PostMapping("/save")
	public CommonResponse save(@RequestBody ConfigDTO config) {
		if (!configService.checkConfigKeyExists(config.getOldConfigKey(), config.getConfigKey())) {
			return renderResult(Global.FALSE, "配置'" + config.getConfigKey() + "'失败，配置键已存在");
		}
		configService.saveOrUpdate(config);
		return renderResult(Global.TRUE, "保存成功");
	}
}
