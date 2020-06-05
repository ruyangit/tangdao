/**
 *
 */
package com.tangdao.core.web.validate;

/**
 * <p>
 * TODO 描述 错误码列表-通用错误码
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum RuleType {

	NULL("参数为空", com.tangdao.core.web.validate.parser.NullParser.class),
	NOT_NULL("参数为空", com.tangdao.core.web.validate.parser.NotNullParser.class),
	EMPTY("参数为空", com.tangdao.core.web.validate.parser.EmptyParser.class),
	NOT_EMPTY("参数不为空", com.tangdao.core.web.validate.parser.NotEmptyParser.class),
	EMAIL("邮箱错误", com.tangdao.core.web.validate.parser.EmailParser.class),
	MOBILE("手机号错误", com.tangdao.core.web.validate.parser.MobileParser.class),
	PATTERN("正则校验失败", com.tangdao.core.web.validate.parser.MactchRegexParser.class),;

	private String message;

	private Class<? extends RuleParser> parser;

	private RuleType(String message, Class<? extends RuleParser> parser) {
		this.message = message;
		this.parser = parser;
	}

	public String getMessage() {
		return message;
	}

	public Class<? extends RuleParser> getParser() {
		return parser;
	}

}
