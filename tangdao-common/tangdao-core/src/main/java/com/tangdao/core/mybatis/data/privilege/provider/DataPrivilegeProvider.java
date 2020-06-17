package com.tangdao.core.mybatis.data.privilege.provider;

import java.util.Map;

/**
 * 
 * @ClassName: DataPrivilegeProvider.java
 * @author: Naughty Guo
 * @date: May 31, 2016
 */
public interface DataPrivilegeProvider {

	/**
	 * get privilege data.
	 * 
	 * @return Map<String,Object>
	 * @author Naughty Guo May 31, 2016
	 */
	Map<String, Object> getPrivilegeData();
}
