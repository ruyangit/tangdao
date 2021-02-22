/**
 *
 */
package com.tangdao.portal.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月11日
 */
@Data
@ConfigurationProperties(prefix = "tangdao")
public class TangdaoProperties {

	/**
	 * 是否演示模式
	 */
	private Boolean demo = false;

}
