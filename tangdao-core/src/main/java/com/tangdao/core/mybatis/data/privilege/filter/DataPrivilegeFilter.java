package com.tangdao.core.mybatis.data.privilege.filter;

import java.util.Map;

/**
 * 
 * @ClassName: DataPrivilegeFilter.java
 * @author: Naughty Guo
 * @date: May 31, 2016
 */
public interface DataPrivilegeFilter {

	/**
	 * set true if you want to enable data privilege with special condition, 
	 * or not set false.
	 * 
	 * @return Map<String,Boolean>
	 * @author Naughty Guo May 31, 2016
	 */
	Map<String, Boolean> getFilterData();
}
