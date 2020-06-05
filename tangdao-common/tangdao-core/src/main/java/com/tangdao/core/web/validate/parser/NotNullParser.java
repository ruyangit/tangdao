/**
 *
 */
package com.tangdao.core.web.validate.parser;

import com.tangdao.core.web.validate.RuleParser;

import cn.hutool.core.lang.Validator;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月4日
 */
public class NotNullParser implements RuleParser {

	@Override
	public Boolean validate(Object value, String... rvs) {
		return Validator.isNotNull(value);
	}

}
