package com.tangdao.developer.response;

import com.tangdao.core.constant.OpenApiCode;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
public class CommonResponse {

	private String code = OpenApiCode.SUCCESS; // 状态码
	private String message; // 成功发送的短信计费条数

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
