/**
 *
 */
package com.tangdao.core.constant;

/**
 * <p>
 * TODO 描述 错误码列表-通用错误码
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum CommonApiCode implements ErrorCode {

	OK(0, "操作成功"), 
	
	
	BAD_REQUEST(1000400, "参数异常"), 
	UNAUTHORIZED(1000401, "用户身份验证失败"), 
	FORBIDDEN(1000403, "无访问权限"), 
	NOT_FOUND(1000404, "找不到访问资源"), 
	BAD_METHOD(1000405, "方法错误"), 
	INTERNAL_ERROR(1000500, "内部异常"),
	BAD_GATEWAY(1000502, "网关异常"),
	UNAVAILABLE(1000503, "服务端拒绝请求"),
	DEMO(1000000, "演示内容，不允许修改"),

	//  --- UAC ---
	UAC1100001(1100001, "会话超时,请刷新页面重试"),
	UAC1100002(1100002, "凭证解析失败"),
	UAC1100003(1100003, "操作频率过快, 您的帐号已被冻结"),
	
	//  --- DEV ---
	DEV7100102(7100102, "用户请求参数不匹配"),
	DEV7100103(7100103, "参数内容编码不正确"),
	DEV7100104(7100104, "时间戳已过期"),
	DEV7100105(7100105, "IP未报备"),
	DEV7100106(7100106, "账户无效"),
	DEV7100107(7100107, "账户冻结或停用"),
	DEV7100108(7100108, "账户鉴权失败"),
	DEV7100109(7100109, "账户计费异常"),
	DEV7100111(7100111, "账户余额不足"),
	DEV7100112(7100112, "手机号码数量超限"),
	;

	private int code;
	private String message;

	private CommonApiCode(int code, String message) {
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

}
