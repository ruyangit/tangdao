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
public enum ErrorApiCode implements IBaseEnum {

	/**
	 * TODO 内部错误
	 */
	InternalError("InternalError", "内部错误"),
	/**
	 * TODO HTTP(S)请求协议错误，只支持 GET 和 POST 请求
	 */
	UnsupportedProtocol("UnsupportedProtocol", "HTTP(S)请求协议错误，只支持 GET 和 POST 请求"),
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
	 * TODO 操作不支持
	 */
	UnsupportedOperation("UnsupportedOperation", "操作不支持"),
	/**
	 * TODO 接口不支持所传地域
	 */
	UnsupportedRegion("UnsupportedRegion", "接口不支持所传地域"),
	/**
	 * TODO 未授权操作
	 */
	Unauthorized("Unauthorized", "未授权操作"),
	/**
	 * TODO 参数错误
	 */
	InvalidParameter("InvalidParameter", "参数错误"),
	/**
	 * TODO 参数取值错误
	 */
	InvalidParameterValue("InvalidParameterValue", "参数取值错误"),
	/**
	 * TODO 接口不存在
	 */
	InvalidAction("InvalidAction", "接口不存在"),
	/**
	 * TODO 资源被占用
	 */
	ResourceInUse("ResourceInUse", "资源被占用"),
	/**
	 * TODO 资源不存在
	 */
	ResourceNotFound("ResourceNotFound", "资源不存在"),
	/**
	 * TODO 资源不可用
	 */
	ResourceUnavailable("ResourceUnavailable", "资源不可用"),
	/**
	 * TODO 资源不足
	 */
	ResourceInsufficient("ResourceInsufficient", "资源不足"),
	/**
	 * TODO 操作失败
	 */
	FailedOperation("FailedOperation", "操作失败"),
	/**
	 * TODO 密钥不存在
	 */
	AuthFailure_SecretNotFound("AuthFailure.SecretNotFound", "密钥不存在"),
	/**
	 * TODO 签名错误
	 */
	AuthFailure_SignatureFailure("AuthFailure.SignatureFailure", "签名错误"),
	/**
	 * TODO 签名过期
	 */
	AuthFailure_SignatureExpire("AuthFailure.SignatureExpire", "签名过期"),
	/**
	 * TODO MFA 错误
	 */
	AuthFailure_MFAFailure("AuthFailure.MFAFailure", "MFA 错误"),
	/**
	 * TODO 未授权操作
	 */
	AuthFailure_Unauthorized("AuthFailure.Unauthorized", "未授权操作"),
	/**
	 * TODO 用户不存在
	 */
	AuthFailure_UserNotFound("AuthFailure.UserNotFound", "用户不存在"),
	/**
	 * TODO 未知用户
	 */
	AuthFailure_UserLoginFailure("AuthFailure.UserLoginFailure", "用户登录错误"),
	/**
	 * TODO 密钥非法
	 */
	AuthFailure_InvalidSecret("AuthFailure.InvalidSecret", "密钥非法"),
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
