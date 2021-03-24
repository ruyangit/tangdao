/**
 *
 */
package com.tangdao.core.web.interceptor;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.tangdao.core.utils.ServletUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
@Intercepts({
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class SqlCommitTypeInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		// 数据操作类型
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		// 用于日志处理
		HttpServletRequest request = ServletUtil.getRequest();
		if (request != null) {
			request.setAttribute(SqlCommandType.class.getName(), mappedStatement.getSqlCommandType());
		}
		return invocation.proceed();
	}

}
