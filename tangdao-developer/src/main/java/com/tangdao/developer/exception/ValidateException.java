/**
 *
 */
package com.tangdao.developer.exception;

import com.tangdao.core.constant.ErrorCode;

import cn.hutool.core.exceptions.StatefulException;

/**
 * <p>
 * TODO 验证接入数据异常
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class ValidateException extends StatefulException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ValidateException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}

}
