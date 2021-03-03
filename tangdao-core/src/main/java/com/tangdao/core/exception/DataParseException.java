/**
 *
 */
package com.tangdao.core.exception;

import com.tangdao.core.constant.ErrorCode;

import cn.hutool.core.exceptions.StatefulException;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月3日
 */
public class DataParseException extends StatefulException {

	private static final long serialVersionUID = 1L;

	public DataParseException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

	/**
	 * 
	 * @param errorCode
	 * @param throwable
	 */
	public DataParseException(ErrorCode errorCode, Throwable throwable) {
		super(errorCode.getCode(), errorCode.getMessage(), throwable);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public DataParseException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * @param string
	 */
	public DataParseException(String message) {
		super(message);
	}

	public DataParseException(Throwable throwable) {
		super(throwable);
	}
}
