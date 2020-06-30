/**
 *
 */
package com.tangdao.core.web.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tangdao.core.mybatis.data.privilege.provider.DataPrivilegeProvider;
import com.tangdao.core.session.SessionContext;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
@Component
public class DemoDataPrivilegeProvider implements DataPrivilegeProvider {

	@Override
	public Map<String, Object> getPrivilegeData() {
		// TODO Auto-generated method stub
		try {
			Map<String, Object> privilegeData = new HashMap<String, Object>();
			privilegeData.put("createByKey", SessionContext.getUserId());
			if(DemoDataPrivilegeContext.getDataPrivilegeProvider()!=null) {
				privilegeData.putAll(DemoDataPrivilegeContext.getDataPrivilegeProvider());
			}
			return privilegeData;
		} finally {
			DemoDataPrivilegeContext.removeDataPrivilegeProvider();
		}
	}
}
