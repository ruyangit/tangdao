/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.service.mapper.ConfigMapper;
import com.tangdao.service.model.domain.Config;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
@Service
public class ConfigService extends BaseService<ConfigMapper, Config> {

	public Config getByConfigKey(String configKey) {
		return super.getOne(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, configKey));
	}
}
