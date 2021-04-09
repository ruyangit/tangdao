/**
 *
 */
package com.tangdao.core.constant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class OpenApiCode {

	// 调用平台成功码
	public static final String SUCCESS = "0";

	// 推送至用户侧成功码
	public static final String DELIVER_SUCCESS = "DELIVRD";

	public enum CommonApiCode implements ErrorCode {
		COMMON_SUCCESS("0", "调用成功"), COMMON_REQUEST_EXCEPTION("C0001", "请求参数异常"),
		COMMON_REQUEST_ENCODING_ERROR("C0002", "参数内容编码不正确"), COMMON_REQUEST_TIMESTAMPS_EXPIRED("C0003", "时间戳已过期"),
		COMMON_REQUEST_IP_INVALID("C0004", "IP未报备"), COMMON_APPKEY_INVALID("C0005", "账户无效"),
		COMMON_APPKEY_NOT_AVAIABLE("C0006", "账户冻结或停用"), COMMON_AUTHENTICATION_FAILED("C0007", "账户鉴权失败"),
		COMMON_BALANCE_EXCEPTION("C0008", "账户计费异常"), COMMON_BALANCE_NOT_ENOUGH("C0009", "账户余额不足"),
		COMMON_BEYOND_MOBILE_THRESHOLD("C0010", "手机号码数量超限（1000个）"), COMMON_ACCESS_DENIED("C0011", "无访问权限"),
		COMMON_ANNOTATION_EXCEPTION("C0012", "注解配置异常"),
		COMMON_DEMO("C0099", "演示服务"), COMMON_SERVER_EXCEPTION("C0100", "未知异常");

		private String code;
		private String message;

		private CommonApiCode(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}

		public static CommonApiCode parse(String code) {
			if (StrUtil.isEmpty(code)) {
				return null;
			}

			for (CommonApiCode api : CommonApiCode.values()) {
				if (api.getCode().equalsIgnoreCase(code)) {
					return api;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return enumToJsonMessage(code, message);
		}
	}

	/**
	 * TODO 枚举信息转JSON格式输出
	 *
	 * @param code
	 * @param message
	 * @return
	 */
	public static String enumToJsonMessage(String code, String message) {
		JSONObject object = new JSONObject();
		object.put("code", code);
		object.put("message", message);
		return JSON.toJSONString(object);
	}
}
