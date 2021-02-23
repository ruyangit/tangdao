/**
 *
 */
package com.tangdao.developer.exception;

import com.tangdao.core.constant.ErrorCode;

import cn.hutool.core.exceptions.StatefulException;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
public class QueueProcessException extends StatefulException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public QueueProcessException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}
}
