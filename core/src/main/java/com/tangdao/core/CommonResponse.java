/**
 *
 */
package com.tangdao.core;

import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;

import com.tangdao.core.constant.ErrorCode;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;

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

	private CommonResponse() {
		super();
		this.success();
	}

	public CommonResponse success() {
		this.success(StringUtils.EMPTY);
		return this;
	}

	public CommonResponse success(String message) {
		this.put(CODE, CommonApiCode.COMMON_SUCCESS.getCode());
		this.put(MESSAGE, message);
		return this;
	}

	public CommonResponse fail(ErrorCode errorCode) {
		this.fail(errorCode.getCode(), errorCode.getMessage());
		return this;
	}

	public CommonResponse fail(String code, String message) {
		this.put(CODE, code);
		this.put(MESSAGE, message);
		return this;
	}

	public CommonResponse setData(Object data) {
		return putData(DATA, data);
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
		commonResponse.setData(data);
		return commonResponse;
	}
}
