/**
 *
 */
package com.tangdao.common;

import java.util.LinkedHashMap;

import com.tangdao.common.constant.IBaseEnum;

/**
 * <p>
 * TODO 描述 工共响应
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月7日
 */
public class CommonResponse extends LinkedHashMap<String, Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String SUCCESS = "success";

	private static final String MESSAGE = "message";

	private static final String CODE = "code";

	private static final String DATA = "data";

	private static final String EMPTY = "";

	public boolean isSuccess() {
		return get(SUCCESS) != null && (Boolean) get(SUCCESS);
	}

	public String getMessage() {
		if (get(MESSAGE) != null) {
			return (String) get(MESSAGE);
		}
		return EMPTY;
	}

	private CommonResponse() {
		super();
		this.put(SUCCESS, false);
	}

	public CommonResponse success() {
		this.put(SUCCESS, true);
		return this;
	}

	public CommonResponse success(String message) {
		this.put(SUCCESS, true);
		this.put(MESSAGE, message);
		return this;
	}

	public CommonResponse fail(String message) {
		this.put(SUCCESS, false);
		this.put(MESSAGE, message);
		return this;
	}
	
	public CommonResponse fail(String code, String message) {
		this.put(SUCCESS, false);
		this.put(CODE, code);
		this.put(MESSAGE, message);
		return this;
	}
	
	public CommonResponse fail(IBaseEnum baseEnum) {
		this.put(SUCCESS, false);
		this.put(CODE, baseEnum.code());
		this.put(MESSAGE, baseEnum.message());
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
