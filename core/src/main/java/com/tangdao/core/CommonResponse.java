/**
 *
 */
package com.tangdao.core;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

import com.tangdao.core.constant.ErrorCode;
import com.tangdao.core.constant.OpenApiCode;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class CommonResponse extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "message";

	private static final String CODE = "code";

	private static final String DATA = "data";

	private static final String RESULT = "result";

	private CommonResponse() {
		super();
		this.success();
	}

	public CommonResponse success() {
		this.success(StringUtils.EMPTY);
		this.result(true);
		return this;
	}

	public CommonResponse result(Boolean result) {
		this.put(RESULT, result);
		return this;
	}

	public CommonResponse code(String code) {
		this.put(CODE, code);
		return this;
	}

	public CommonResponse message(String message) {
		this.put(MESSAGE, message);
		return this;
	}

	public CommonResponse data(Object data) {
		this.put(DATA, data);
		return this;
	}

	public CommonResponse success(String message) {
		this.code(OpenApiCode.SUCCESS);
		this.message(message);
		return this;
	}

	public CommonResponse success(String message, Object data) {
		this.success(message);
		this.data(data);
		return this;
	}

	public CommonResponse fail(ErrorCode errorCode) {
		this.fail(errorCode.getCode(), errorCode.getMessage());
		return this;
	}

	public CommonResponse fail(String code, String message) {
		this.code(code);
		this.message(message);
		this.result(false);
		return this;
	}

	public CommonResponse putData(String key, Object data) {
		this.put(key, data);
		return this;
	}

	public static CommonResponse createCommonResponse() {
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.success();
		return commonResponse;
	}

	public static CommonResponse createCommonResponse(Object data) {
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.success();
		commonResponse.data(data);
		return commonResponse;
	}
}
