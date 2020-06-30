/**
 *
 */
package com.tangdao.core.mybatis.data.privilege.handler;

import com.tangdao.core.mybatis.data.privilege.DataPrivilegeContext;
import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
public class DefaultDataPrivilegeAnnotationHandler implements DataPrivilegeAnnotationHandler {

	@Override
	public DataPrivilege getDataPrivilegeAnnotation() {
		// TODO Auto-generated method stub
		try {
			DataPrivilege dataPrivilege = DataPrivilegeContext.getDataPrivilege();
			return dataPrivilege;
		} finally {
			DataPrivilegeContext.removeDataPrivilege();
		}
	}
}
