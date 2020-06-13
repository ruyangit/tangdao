/**
 *
 */
package com.tangdao.common.constant;

/**
 * <p>
 * TODO 描述 错误码列表-通用错误码
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum CommonApiCode implements ErrorCode {
	
	OK("200", "执行成功"),
	
	BAD_REQUEST("400", "非法参数"),
	UNAUTHORIZED("401", "用户身份验证失败"),
	FORBIDDEN("403", "不允许访问"),
	INTERNAL_ERROR("500", "内部异常"),
	
	USER_TOKEN_EXPIRE("1007", "用户凭证已过期"),
	;
	
	private String code;
	private String message;

	private CommonApiCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

}
