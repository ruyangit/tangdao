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
public class NullCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		if (Boolean.valueOf(value.toLowerCase())) {
			return key == null;
		} else {
			return key != null;
		}
	}

}
