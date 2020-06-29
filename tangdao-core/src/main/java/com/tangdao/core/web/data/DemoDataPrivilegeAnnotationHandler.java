/**
 *
 */
package com.tangdao.core.web.data;

import org.springframework.stereotype.Component;

import com.tangdao.core.mybatis.data.privilege.annotation.DataPrivilege;
import com.tangdao.core.mybatis.data.privilege.handler.DataPrivilegeAnnotationHandler;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
@Component
public class DemoDataPrivilegeAnnotationHandler implements DataPrivilegeAnnotationHandler {

	@Override
	public DataPrivilege getDataPrivilegeAnnotation() {
		// TODO Auto-generated method stub
		return null;
	}
}
