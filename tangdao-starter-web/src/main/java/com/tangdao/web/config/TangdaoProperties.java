/**
 *
 */
package com.tangdao.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import cn.hutool.core.util.StrUtil;
import lombok.Setter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月11日
 */
@Setter
@ConfigurationProperties
public class TangdaoProperties {

	@Value("${user.sa:ruyang}")
	private String sa;

	public String sa() {
		return sa;
	}

	public boolean isa(String username) {
		return StrUtil.equals(sa, username);
	}
}
