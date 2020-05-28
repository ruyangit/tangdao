/**
 *
 */
package com.tangdao.common.constant;

import com.tangdao.common.utils.StringUtils;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月23日
 */
public class CommonContext {

	/**
	 * 
	 * <p>
	 * TODO 错误码列表-通用错误码
	 * </p>
	 *
	 * @author ruyang@gmail.com
	 * @since 2020年5月14日
	 */
	public enum CommonApiCode implements IBaseEnum {
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
		UnauthorizedOperation("UnauthorizedOperation", "未授权操作"),
		/**
		 * TODO 未知参数错误
		 */
		UnknownParameter("UnknownParameter", "未知参数错误"),
		/**
		 * TODO 缺少参数错误
		 */
		MissingParameter("MissingParameter", "缺少参数错误"),
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
		AuthFailure_SecretIdNotFound("AuthFailure.SecretIdNotFound", "密钥不存在"),
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
		AuthFailure_UnauthorizedOperation("AuthFailure.UnauthorizedOperation", "未授权操作"),
		/**
		 * TODO 用户信息为空
		 */
		AuthFailure_UserEmpty("AuthFailure.UserEmpty", "用户信息为空"),
		/**
		 * TODO 密钥非法
		 */
		AuthFailure_InvalidSecretId("AuthFailure.InvalidSecretId", "密钥非法"),
		/**
		 * TODO token 错误
		 */
		AuthFailure_TokenFailure("AuthFailure.TokenFailure", "token 错误"),;

		private String value;
		private String reasonPhrase;

		private CommonApiCode(String value, String reasonPhrase) {
			this.value = value;
			this.reasonPhrase = reasonPhrase;
		}

		public static CommonApiCode parse(String value) {
			if (StringUtils.isEmpty(value)) {
				return null;
			}
			for (CommonApiCode api : CommonApiCode.values()) {
				if (api.value().equalsIgnoreCase(value)) {
					return api;
				}
			}
			return null;
		}

		@Override
		public String value() {
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public String reasonPhrase() {
			// TODO Auto-generated method stub
			return reasonPhrase;
		}
	}

	public enum DataStatusCode implements IBaseEnum {
		NORMAL("0", "正常"), DELETE("1", "删除"), DISABLE("2", "停用"), FREEZE("3", "冻结"), DRAFT("9", "草稿");

		private String value;
		private String reasonPhrase;

		private DataStatusCode(String value, String reasonPhrase) {
			this.value = value;
			this.reasonPhrase = reasonPhrase;
		}

		@Override
		public String value() {
			// TODO Auto-generated method stub
			return value;
		}

		@Override
		public String reasonPhrase() {
			// TODO Auto-generated method stub
			return reasonPhrase;
		}
	}

}
