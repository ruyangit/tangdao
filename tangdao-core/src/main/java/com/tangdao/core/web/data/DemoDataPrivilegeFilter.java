/**
 *
 */
package com.tangdao.core.web.data;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.tangdao.core.mybatis.data.privilege.filter.DataPrivilegeFilter;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
@Component
public class DemoDataPrivilegeFilter implements DataPrivilegeFilter {

	@Override
	public Map<String, Boolean> getFilterData() {
		// TODO Auto-generated method stub
		Map<String, Boolean> filterData = new HashMap<String, Boolean>();
		filterData.put("createByKey", Boolean.TRUE);
		return filterData;
	}

}
