/**
 *
 */
package com.tangdao.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.tangdao.core.DateMetaObjectHandler;
import com.tangdao.core.annotation.TableScan;
import com.tangdao.core.web.interceptor.SqlCommitTypeInterceptor;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月5日
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.tangdao.*.mapper")
@TableScan("com.tangdao.*.model.domain")
public class MybatisConfig {
	
	/**
	 * 
	 * TODO 分页拦截器
	 * 
	 * @return
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();

		// 分页拦截器
		PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
		// 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求 默认false
		// paginationInnerInterceptor.setOverflow(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		paginationInnerInterceptor.setMaxLimit(500L);

		// add
		mybatisPlusInterceptor.addInnerInterceptor(paginationInnerInterceptor);
		return mybatisPlusInterceptor;
	}

	/**
	 * 
	 * TODO 通用字段属性值填充
	 * 
	 * @return
	 */
	@Bean
	public DateMetaObjectHandler dateMetaObjectHandler() {
		return new DateMetaObjectHandler();
	}

	/**
	 * 
	 * TODO
	 * 
	 * @return
	 */
	@Bean
	public SqlCommitTypeInterceptor sqlCommitTypeInterceptor() {
		return new SqlCommitTypeInterceptor();
	}

}
