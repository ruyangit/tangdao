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
	UAC1100004(1100004, "用户主键不能为空"),
	
	//  --- ODS ---
	ODS1100001(3100001, "获取地址信息失败"),
	ODS1100002(3100002, "找不到该地址信息"),
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
