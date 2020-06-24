package com.tangdao.core.mybatis.data.privilege.interceptor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger LOGGER = LoggerFactory.getLogger(DataPrivilegeInterceptor.class);
	private static final String SQL = "sql";
	private DataPrivilegeFilter dataPrivilegeFilter;
	private DataPrivilegeProvider dataPrivilegeProvider;
	private DataPrivilegeAnnotationHandler dataPrivilegeAnnotationHandler;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Field sqlField = null;
		StatementHandler statementHandler = null;
		DataPrivilegeParameter dataPrivilegeParameter = null;
		DataPrivilege dataPrivilege = dataPrivilegeAnnotationHandler.getDataPrivilegeAnnotation();
		if (null != dataPrivilege) {
			statementHandler = (StatementHandler) invocation.getTarget();
			BoundSql boundSql = statementHandler.getBoundSql();
			dataPrivilegeParameter = new DataPrivilegeParameter();
			MDataPrivilege mDataPrivilege = MDataPrivilegeUtil.create(dataPrivilege);
			dataPrivilegeParameter.setFilterCategory(dataPrivilegeFilter.getFilterData());
			dataPrivilegeParameter.setMDataPrivilege(mDataPrivilege);
			dataPrivilegeParameter.setOriginalSql(statementHandler.getBoundSql().getSql());
			dataPrivilegeParameter.setPrivilegeData(dataPrivilegeProvider.getPrivilegeData());
			String privilegeSql = DataPrivilegeUtil.getPrivilegeSql(dataPrivilegeParameter);
			LOGGER.warn("Privilege sql is: {}", privilegeSql);
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
