/**
 * 
 */
package com.tangdao.module.security.condition;

/**
 * <p>
 * TODO Principal
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月27日
 */
public class EqualsPrincipalCondition extends Condition {

	@Override
	public Boolean fullfills(String conditionKey, Request request) {
		// TODO Auto-generated method stub
		if (!request.context.containsKey(conditionKey)) {
			return false;
		}
        String value = request.context.getProperty(conditionKey);
        return request.principal.equals(value);
	}

}
