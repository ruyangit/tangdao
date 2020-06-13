/**
 *
 */
package com.tangdao.web.config;

import org.springframework.beans.factory.annotation.Value;
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
@ConfigurationProperties
public class TangdaoProperties{

	@Value("${user.superAdmin:ruyang}")
	private String superAdmin;
}
