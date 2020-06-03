/**
 *
 */
package com.tangdao.core.validate;

/**
 * <p>
 * TODO 描述 错误码列表-通用错误码
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum RuleType {

	/**
	 * TODO 内部错误
	 */
	IS_NULL("isNull", "参数不能位空"),
	MAX("max", "最大值"),
	MIN("min", "最小值"),
	PATTERN("pattern", "正则"),
	IS_EMAIL("isEmail", "邮箱"),
	IS_MOBILE("isMobile", "手机号"),
	;

	private String value;
	private String message;

	private RuleType(String value, String message) {
		this.value = value;
		this.message = message;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getMessage() {
		return message;
	}

}
