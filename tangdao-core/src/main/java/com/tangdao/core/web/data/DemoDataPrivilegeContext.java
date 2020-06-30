/**
 *
 */
package com.tangdao.core.web.data;

import java.util.Map;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;

import cn.hutool.core.thread.threadlocal.NamedThreadLocal;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月30日
 */
public class DemoDataPrivilegeContext {

	private static final ThreadLocal<DataPrivilege> dataPrivilegeLocal = new NamedThreadLocal<DataPrivilege>(
			"dataPrivilege");
	private static final ThreadLocal<Map<String, Boolean>> dataPrivilegeFilterLocal = new NamedThreadLocal<Map<String, Boolean>>(
			"dataPrivilegeFilter");
	private static final ThreadLocal<Map<String, Object>> dataPrivilegeProviderLocal = new NamedThreadLocal<Map<String, Object>>(
			"dataPrivilegeProvider");

	public static DataPrivilege getDataPrivilege() {
		return dataPrivilegeLocal.get();
	}

	public static void setDataPrivilege(DataPrivilege dataPrivilege) {
		dataPrivilegeLocal.set(dataPrivilege);
	}

	public static void removeDataPrivilege() {
		dataPrivilegeLocal.remove();
	}

	public static Map<String, Boolean> getDataPrivilegeFilter() {
		return dataPrivilegeFilterLocal.get();
	}

	public static void setDataPrivilegeFilter(Map<String, Boolean> dataPrivilegeFilter) {
		dataPrivilegeFilterLocal.set(dataPrivilegeFilter);
	}

	public static void removeDataPrivilegeFilter() {
		dataPrivilegeFilterLocal.remove();
	}

	public static Map<String, Object> getDataPrivilegeProvider() {
		return dataPrivilegeProviderLocal.get();
	}

	public static void setDataPrivilegeProvider(Map<String, Object> dataPrivilegeProvider) {
		dataPrivilegeProviderLocal.set(dataPrivilegeProvider);
	}

	public static void removeDataPrivilegeProvider() {
		dataPrivilegeProviderLocal.remove();
	}
}
