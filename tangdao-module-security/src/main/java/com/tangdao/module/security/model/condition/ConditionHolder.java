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
public class ConditionHolder {

	private final Map<String, Condition> conditions;

	@Autowired
	public ConditionHolder(Map<String, Condition> conditions) {
		this.conditions = conditions;
	}

	public Condition findCondition(String name) {
		return conditions.get(name);
	}

}
