/**
 *
 */
package com.tangdao.core.web.data;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.tangdao.core.mybatis.data.privilege.provider.DataPrivilegeProvider;

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
		return null;
	}
}
