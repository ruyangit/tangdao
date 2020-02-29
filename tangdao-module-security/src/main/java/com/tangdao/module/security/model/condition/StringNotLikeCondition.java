/**
 * 
 */
package com.tangdao.module.security.model.condition;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.tangdao.common.utils.StringUtils;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月29日
 */
@Component
public class StringNotLikeCondition extends Condition {

	@Override
	public boolean evaluate(String key, String value) {
		// TODO Auto-generated method stub
		String pattern = StringUtils.patternFromGlob(value);
		return key == null || !Pattern.matches(pattern, key);
	}

}
