/**
 *
 */
package com.tangdao.core.exception;

import com.tangdao.core.constant.ErrorCode;

import cn.hutool.core.exceptions.StatefulException;

/**
 * <p>
 * TODO 列队处理异常
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
public class QueueProcessException extends StatefulException {

	private static final long serialVersionUID = 1L;

	public QueueProcessException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

	/**
	 * @param string
	 */
	public QueueProcessException(String message) {
		super(message);
	}
}
