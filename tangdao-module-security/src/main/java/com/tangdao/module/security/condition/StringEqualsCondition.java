/**
 * 
 */
package com.tangdao.module.security.condition;

/**
 * <p>
 * TODO Equal
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public class StringEqualsCondition extends Condition {

	public StringEqualsCondition(String value) {
		this.options.setProperty("equals", value);
	}

	public StringEqualsCondition() {
	}

	@Override
	public Boolean fullfills(String conditionKey, Request request) {
		// TODO Auto-generated method stub
		if (!request.context.containsKey(conditionKey)) {
			return false;
		}

		String value = request.context.getProperty(conditionKey);
		String conditionValue = this.options.getProperty("equals");
		return conditionValue.equals(value);
	}

}
