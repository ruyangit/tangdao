/**
 *
 */
package com.tangdao.core.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.tangdao.core.mybatis.data.privilege.filter.DataPrivilegeFilter;
import com.tangdao.core.mybatis.data.privilege.handler.DataPrivilegeAnnotationHandler;
import com.tangdao.core.mybatis.data.privilege.interceptor.DataPrivilegeInterceptor;
import com.tangdao.core.mybatis.data.privilege.provider.DataPrivilegeProvider;
import com.tangdao.core.mybatis.injector.DateMetaObjectHandler;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Configuration
@MapperScan("com.tangdao.modules.*.mapper")
public class MyBatisAutoConfigurer {

	/**
	 * 分页拦截
	 * @return
	 */
	@Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
        // paginationInterceptor.setOverflow(false);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
		paginationInterceptor.setLimit(500);
        // 开启 count 的 join 优化,只针对部分 left join
        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
        return paginationInterceptor;
    }
	
	@Autowired
	private DataPrivilegeAnnotationHandler dataPrivilegeAnnotationHandler;
	
	@Autowired
	private DataPrivilegeFilter dataPrivilegeFilter;
	
	@Autowired
	private DataPrivilegeProvider dataPrivilegeProvider;
	
	/**
	 * 数据权限拦截
	 * @return
	 */
	@Bean
	public DataPrivilegeInterceptor dataPrivilegeInterceptor() {
		DataPrivilegeInterceptor dataPrivilegeInterceptor = new DataPrivilegeInterceptor();
		dataPrivilegeInterceptor.setDataPrivilegeAnnotationHandler(dataPrivilegeAnnotationHandler);
		dataPrivilegeInterceptor.setDataPrivilegeFilter(dataPrivilegeFilter);
		dataPrivilegeInterceptor.setDataPrivilegeProvider(dataPrivilegeProvider);
		return dataPrivilegeInterceptor;
	}
	
	/**
	 * 数据补全
	 * @return
	 */
	@Bean
    @ConditionalOnMissingBean
    public MetaObjectHandler metaObjectHandler() {
        return new DateMetaObjectHandler();
    }
}
