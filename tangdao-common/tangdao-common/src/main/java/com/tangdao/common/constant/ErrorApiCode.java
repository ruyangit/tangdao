/**
 *
 */
package com.tangdao.common.constant;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述 错误码列表-通用错误码
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public enum ErrorApiCode implements ErrorCode {

	/**
	 * TODO 内部错误
	 */
	InternalError("InternalError", "内部错误"),
	/**
	 * TODO HTTP(S)请求协议错误，只支持 GET 和 POST 请求
	 */
	MethodNotSupported("MethodNotSupported", "HTTP(S)请求协议错误，只支持 GET 和 POST 请求"),
	/**
	 * TODO 请求的次数超过了频率限制
	 */
	RequestLimitExceeded("RequestLimitExceeded", "请求的次数超过了频率限制"),
	/**
	 * TODO 超过配额限制
	 */
	LimitExceeded("LimitExceeded", "超过配额限制"),
	/**
	 * TODO 接口版本不存在
	 */
	NoSuchVersion("NoSuchVersion", "接口版本不存在"),
	/**
	 * TODO 参数错误
	 */
	InvalidParameter("InvalidParameter", "参数错误"),
	/**
	 * TODO 参数取值错误
	 */
	InvalidParameterValue("InvalidParameterValue", "参数取值错误"),
	/**
	 * TODO 资源不存在
	 */
	ResourceNotFound("ResourceNotFound", "资源不存在"),
	/**
	 * TODO 资源不可用
	 */
	ResourceUnavailable("ResourceUnavailable", "资源不可用"),
	/**
	 * TODO 未授权操作
	 */
	AuthFailure_Unauthorized("AuthFailure.Unauthorized", "未授权操作"),
	/**
	 * TODO 无权限访问
	 */
	AuthFailure_Forbidden("AuthFailure.Forbidden", "无权限访问"),
	/**
	 * TODO 用户不存在
	 */
	AuthFailure_UserNotFound("AuthFailure.UserNotFound", "用户不存在"),
	/**
	 * TODO token failure
	 */
	AuthFailure_TokenFailure("AuthFailure.TokenFailure", "Token Failure"),
	/**
	 * TODO token expired
	 */
	AuthFailure_TokenExpire("AuthFailure.TokenExpire", "Token Expire"),;

	private String code;
	private String message;

	private ErrorApiCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorApiCode parse(String code) {
		if (StrUtil.isEmpty(code)) {
			return null;
		}
		for (ErrorApiCode api : ErrorApiCode.values()) {
			if (api.code().equalsIgnoreCase(code)) {
				return api;
			}
		}
		return null;
	}

	@Override
	public String code() {
		// TODO Auto-generated method stub
		return code;
	}

	@Override
	public String message() {
		// TODO Auto-generated method stub
		return message;
	}
}
