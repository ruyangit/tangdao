/**
 *
 */
package com.tangdao.core.auth;

import com.tangdao.common.constant.ErrorCode;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月14日
 */
public class AccessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AccessException() {

	}

	public AccessException(String message) {
		super(message);
	}

	public AccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public AccessException(Throwable cause) {
		super(cause);
	}

	private ErrorCode errorCode;

	public AccessException(ErrorCode errorCode) {
		this(errorCode.message());
		this.errorCode = errorCode;
	}

	public AccessException(ErrorCode errorCode, String message) {
		this(message);
		this.errorCode = errorCode;
	}

	public AccessException(ErrorCode errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
