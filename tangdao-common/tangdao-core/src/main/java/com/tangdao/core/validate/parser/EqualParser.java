/**
 *
 */
package com.tangdao.core.validate.parser;

import com.tangdao.core.validate.RuleParser;

import cn.hutool.core.lang.Validator;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月4日
 */
public class EqualParser implements RuleParser {

	@Override
	public Boolean validate(Object value, String... rvs) {
		return Validator.equal(value, rvs[0]);
	}

}
