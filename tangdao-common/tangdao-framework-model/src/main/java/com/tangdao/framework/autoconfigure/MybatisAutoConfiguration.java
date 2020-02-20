/**
 *
 */
package com.tangdao.framework.autoconfigure;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.tangdao.framework.plugins.DataSqlParserHandler;

/**
 *  mybatis 加载
 * @author Ryan Ru(ruyangit@gmail.com)
 */

@Configuration
@AutoConfigureAfter(MybatisPlusAutoConfiguration.class)
@EnableConfigurationProperties(MybatisProperties.class)
public class MybatisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DataSqlParserHandler dataSqlParserHandler() {
		return new DataSqlParserHandler();
	}
}
