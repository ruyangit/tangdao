/**
 * 
 */
package com.tangdao.framework.plugins;

import java.sql.Connection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.tangdao.common.web.ServletUtils;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月20日
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class DataSqlParserHandler extends AbstractSqlParserHandler implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		// SQL 解析
		this.sqlParser(metaObject);

		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		HttpServletRequest request = ServletUtils.getRequest();
		if (request != null) {
			request.setAttribute(SqlCommandType.class.getName(), mappedStatement.getSqlCommandType());
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		// 生成代理对象
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
	}
	
}
