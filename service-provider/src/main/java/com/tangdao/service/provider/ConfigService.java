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

	public boolean checkConfigKeyExists(String oldConfigKey, String configKey) {
		if (configKey != null && configKey.equals(oldConfigKey)) {
			return true;
		} else if (configKey != null
				&& super.count(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, configKey)) == 0) {
			return true;
		}
		return false;
	}

}
