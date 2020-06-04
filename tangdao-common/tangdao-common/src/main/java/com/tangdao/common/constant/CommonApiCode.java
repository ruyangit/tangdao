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
	
	SUCCESS("200", "执行成功"),
	FAILED("-1", "操作失败"),
	
	BAD_REQUEST("400", "非法参数"),
	UNAUTHORIZED("401", "授权访问失败"),
	FORBIDDEN("403", "暂无权限"),
	INTERNAL_ERROR("500", "内部异常"),
	
	AUTH_EXPIRE("1007", "授权凭证过期"),
	AUTH_REMOTE_LOGIN("1008", "异地登录异常"),
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
