/**
 * 
 */
package com.tangdao.module.security.condition;

import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * <p>
 * TODO 条件
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @JsonSubTypes.Type(value = StringEqualsCondition.class, name = "StringEqualsCondition"),
		@JsonSubTypes.Type(value = EqualsPrincipalCondition.class, name = "EqualsPrincipalCondition") })
public abstract class Condition {

	public Properties options = new Properties();

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Properties getOptions() {
		return options;
	}

	public void setOptions(Properties options) {
		this.options = options;
	}

	abstract public Boolean fullfills(String conditionKey, Request request);
}
