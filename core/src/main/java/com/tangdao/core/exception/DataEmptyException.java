/**
 *
 */
package com.tangdao.core.exception;

import com.tangdao.core.constant.ErrorCode;

/**
 * <p>
 * TODO 列队处理异常
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
public class DataEmptyException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public DataEmptyException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

	/**
	 * 
	 * @param errorCode
	 * @param throwable
	 */
	public DataEmptyException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode.getCode(), errorCode.getMessage(), throwable);
	}
	
	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public DataEmptyException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param string
	 */
	public DataEmptyException(String message) {
		super(message);
	}
}
