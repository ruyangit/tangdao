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
public class NumericLessThanCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		try {
			return key != null && Integer.valueOf(key).compareTo(Integer.valueOf(value)) < 0;
		} catch (NumberFormatException e) {
			try {
				return Double.valueOf(key).compareTo(Double.valueOf(value)) < 0;
			} catch (NumberFormatException e1) {
				// It does not make sense to check the equality of two floats.
			}
		}
		return false;
	}

}
