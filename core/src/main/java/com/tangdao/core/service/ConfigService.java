/**
 *
 */
package com.tangdao.core.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.dao.ConfigMapper;
import com.tangdao.core.model.domain.Config;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月29日
 */
public class ConfigService extends BaseService<ConfigMapper, Config> {

	public Config getByKey(String key) {
		return super.getOne(Wrappers.<Config>lambdaQuery().eq(Config::getConfigKey, key));
	}
}
