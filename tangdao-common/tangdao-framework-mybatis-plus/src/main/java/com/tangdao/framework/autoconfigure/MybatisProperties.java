/**
 *
 */
package com.tangdao.framework.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @author Ryan Ru(ruyangit@gmail.com)
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "tangdao.mybatis")
public class MybatisProperties {

}
