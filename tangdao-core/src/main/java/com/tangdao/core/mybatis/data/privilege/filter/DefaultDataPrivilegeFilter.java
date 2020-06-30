/**
 *
 */
package com.tangdao.core.mybatis.data.privilege.filter;

import java.util.HashMap;
import java.util.Map;

import com.tangdao.core.mybatis.data.privilege.DataPrivilegeContext;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月30日
 */
public class DefaultDataPrivilegeFilter implements DataPrivilegeFilter {

	@Override
	public Map<String, Boolean> getFilterData() {
		// TODO Auto-generated method stub
		try {
			Map<String, Boolean> filterData = new HashMap<String, Boolean>();
			filterData.put("createByKey", Boolean.TRUE);
			if (DataPrivilegeContext.getDataPrivilegeFilter() != null) {
				filterData.putAll(DataPrivilegeContext.getDataPrivilegeFilter());
			}
			return filterData;
		} finally {
			DataPrivilegeContext.removeDataPrivilegeFilter();
		}
	}

}
