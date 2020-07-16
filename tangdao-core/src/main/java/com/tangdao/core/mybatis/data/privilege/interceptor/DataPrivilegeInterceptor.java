package com.tangdao.core.mybatis.data.privilege.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.filter.DataPrivilegeFilter;
import com.tangdao.core.mybatis.data.privilege.handler.DataPrivilegeAnnotationHandler;
import com.tangdao.core.mybatis.data.privilege.model.DataPrivilegeParameter;
import com.tangdao.core.mybatis.data.privilege.model.MDataPrivilege;
import com.tangdao.core.mybatis.data.privilege.provider.DataPrivilegeProvider;
import com.tangdao.core.mybatis.data.privilege.util.DataPrivilegeUtil;
import com.tangdao.core.mybatis.data.privilege.util.MDataPrivilegeUtil;

/**
 * 
 * @ClassName: DataPrivilegeInterceptor.java
 * @author: Naughty Guo
 * @date: May 31, 2016
 */
@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class })
})
public class DataPrivilegeInterceptor implements Interceptor {

	private static final String SQL = "sql";
	private DataPrivilegeFilter dataPrivilegeFilter;
	private DataPrivilegeProvider dataPrivilegeProvider;
	private DataPrivilegeAnnotationHandler dataPrivilegeAnnotationHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
		// 数据操作类型
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		// 用于日志处理
		HttpServletRequest request = WebUtils.getRequest();
		if (request != null) {
			request.setAttribute(SqlCommandType.class.getName(), mappedStatement.getSqlCommandType());
		}
		
		// 不是查询或者是存储过程直接跳过
		if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType() || StatementType.CALLABLE == mappedStatement.getStatementType()) {
			return invocation.proceed();
		}
		
		Field sqlField = null;
		DataPrivilegeParameter dataPrivilegeParameter = null;
		DataPrivilege dataPrivilege = dataPrivilegeAnnotationHandler.getDataPrivilegeAnnotation();
		if (null != dataPrivilege) {
			BoundSql boundSql = statementHandler.getBoundSql();
			dataPrivilegeParameter = new DataPrivilegeParameter();
			MDataPrivilege mDataPrivilege = MDataPrivilegeUtil.create(dataPrivilege);
			dataPrivilegeParameter.setFilterCategory(dataPrivilegeFilter.getFilterData());
			dataPrivilegeParameter.setMDataPrivilege(mDataPrivilege);
			dataPrivilegeParameter.setOriginalSql(statementHandler.getBoundSql().getSql());
			dataPrivilegeParameter.setPrivilegeData(dataPrivilegeProvider.getPrivilegeData());
			String privilegeSql = DataPrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter);
			sqlField = boundSql.getClass().getDeclaredField(SQL);
			sqlField.setAccessible(true);
			sqlField.set(boundSql, privilegeSql);
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		
	}

	public DataPrivilegeFilter getDataPrivilegeFilter() {
		return dataPrivilegeFilter;
	}

	public void setDataPrivilegeFilter(DataPrivilegeFilter dataPrivilegeFilter) {
		this.dataPrivilegeFilter = dataPrivilegeFilter;
	}

	public DataPrivilegeProvider getDataPrivilegeProvider() {
		return dataPrivilegeProvider;
	}

	public void setDataPrivilegeProvider(DataPrivilegeProvider dataPrivilegeProvider) {
		this.dataPrivilegeProvider = dataPrivilegeProvider;
	}

	public DataPrivilegeAnnotationHandler getDataPrivilegeAnnotationHandler() {
		return dataPrivilegeAnnotationHandler;
	}

	public void setDataPrivilegeAnnotationHandler(DataPrivilegeAnnotationHandler dataPrivilegeAnnotationHandler) {
		this.dataPrivilegeAnnotationHandler = dataPrivilegeAnnotationHandler;
	}
}
