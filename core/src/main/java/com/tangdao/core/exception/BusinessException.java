package com.tangdao.core.exception;

import com.tangdao.core.constant.ErrorCode;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// 异常状态码
	private Object code;

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(Throwable throwable) {
		super(throwable);
	}

	public BusinessException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public BusinessException(Object code, String message) {
		super(message);
		this.code = code;
	}

	public BusinessException(Object code, Throwable throwable) {
		super(throwable);
		this.code = code;
	}

	public BusinessException(Object code, String message, Throwable throwable) {
		super(message, throwable);
		this.code = code;
	}

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.code = errorCode.getCode();
	}

	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.code = errorCode.getCode();
	}

	/**
	 * @return 获得异常状态码
	 */
	public Object getCode() {
		return code;
	}
}
