package com.tangdao.core.mybatis.data.privilege.handler;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;

/**
 * 
 * @ClassName: DataPrivilegeAnnotationHandler.java
 * @author: Naughty Guo
 * @date: Mar 4, 2018
 */
public interface DataPrivilegeAnnotationHandler {

	/**
	 * get DataPrivilege annotation.
	 *
	 * @return DataPrivilege
	 * @author Naughty Guo 
	 * @date Mar 4, 2018
	 */
	DataPrivilege getDataPrivilegeAnnotation();
}
