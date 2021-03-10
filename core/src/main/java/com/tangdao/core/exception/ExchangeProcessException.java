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
public class ExchangeProcessException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ExchangeProcessException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

	/**
	 * @param string
	 */
	public ExchangeProcessException(String message) {
		super(message);
	}
	
	public ExchangeProcessException(Throwable throwable) {
		super(throwable);
	}
}
