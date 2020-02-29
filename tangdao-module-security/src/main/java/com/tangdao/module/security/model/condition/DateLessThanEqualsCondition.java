/**
 * 
 */
package com.tangdao.module.security.model.condition;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.tangdao.module.security.key.Iso8601DateParser;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月29日
 */
@Component
public class DateLessThanEqualsCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		try {
			return key != null && Iso8601DateParser.parse(key).compareTo(Iso8601DateParser.parse(value)) <= 0;
		} catch (ParseException e) {
			return false;
		}
	}

}
