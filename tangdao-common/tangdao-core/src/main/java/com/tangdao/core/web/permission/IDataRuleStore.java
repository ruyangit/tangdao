/**
 *
 */
package com.tangdao.core.web.permission;

import java.util.Set;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
public interface IDataRuleStore {

	/**
	 * TODO 规则存储
	 * @param rules
	 */
	void store(Set<DataRuleInfo> rules);
}
