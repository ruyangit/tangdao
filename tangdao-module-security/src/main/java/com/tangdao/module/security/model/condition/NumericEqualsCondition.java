/**
 * 
 */
package com.tangdao.module.security.model.condition;

import org.springframework.stereotype.Component;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月29日
 */
@Component
public class NumericEqualsCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		try {
			return key != null && Integer.valueOf(key).equals(Integer.valueOf(value));
		} catch (NumberFormatException e) {
			// It does not make sense to check the equality of two floats.
		}
		return false;
	}

}
