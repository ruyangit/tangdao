/**
 * 
 */
package com.tangdao.module.security.model.condition;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月28日
 */
@Component
public class ConditionProcessHolder {

	private final Map<String, Condition> conditions;

	@Autowired
	public ConditionProcessHolder(Map<String, Condition> conditions) {
		this.conditions = conditions;
	}

	public Condition getCondition(String name) {
		name = name.substring(0, 1).toLowerCase() + name.substring(1);
		return conditions.get(name + Condition.BEAN_SUFFIX);
	}
}
