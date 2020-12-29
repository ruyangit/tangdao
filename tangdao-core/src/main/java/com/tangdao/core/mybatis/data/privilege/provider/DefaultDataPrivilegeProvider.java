/**
 *
 */
package com.tangdao.core.mybatis.data.privilege.provider;

import java.util.HashMap;
import java.util.Map;

import com.tangdao.core.mybatis.data.privilege.DataPrivilegeContext;
import com.tangdao.core.session.SessionContext;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
public class DefaultDataPrivilegeProvider implements DataPrivilegeProvider {

	@Override
	public Map<String, Object> getPrivilegeData() {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> privilegeData = new HashMap<String, Object>();
			privilegeData.put("createByKey", SessionContext.getId());
			if(DataPrivilegeContext.getDataPrivilegeProvider()!=null) {
				privilegeData.putAll(DataPrivilegeContext.getDataPrivilegeProvider());
			}
			return privilegeData;
		} finally {
			DataPrivilegeContext.removeDataPrivilegeProvider();
		}
	}
}
