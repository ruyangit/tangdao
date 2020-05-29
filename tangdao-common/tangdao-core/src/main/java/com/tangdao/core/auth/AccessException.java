/**
 *
 */
package com.tangdao.core.auth;

import com.tangdao.common.constant.IBaseEnum;

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

	private IBaseEnum baseEnum;

	public AccessException(IBaseEnum baseEnum) {
		this(baseEnum.message());
		this.baseEnum = baseEnum;
	}

	public AccessException(IBaseEnum baseEnum, String message) {
		this(message);
		this.baseEnum = baseEnum;
	}

	public AccessException(IBaseEnum baseEnum, Throwable cause) {
		super(cause);
		this.baseEnum = baseEnum;
	}

	public IBaseEnum getBaseEnum() {
		return baseEnum;
	}

}
