/**
 *
 */
package com.tangdao.developer.exception;

import com.tangdao.core.constant.ErrorCode;

/**
 * <p>
 * TODO 列队处理异常
 * </p>
 *
 * @author ruyang
 * @since 2021年2月24日
 */
public class ValidateException extends cn.hutool.core.exceptions.ValidateException {

	private static final long serialVersionUID = 1L;

	public ValidateException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}
}
