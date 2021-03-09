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
public class BusinessException extends StatefulException {

	private static final long serialVersionUID = 1L;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

	/**
	 * @param string
	 */
	public BusinessException(String message) {
		super(message);
	}
}
