/**
 *
 */
package com.tangdao.common.exception;

import com.tangdao.common.constant.IBaseEnum;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月14日
 */
public class Security403Exception extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public Security403Exception() {
		
	}
	
	public Security403Exception(String message) {
		super(message);
	}

	public Security403Exception(String message, Throwable cause) {
		super(message, cause);
	}

	public Security403Exception(Throwable cause) {
		super(cause);
	}
	
	private IBaseEnum baseEnum;
	
	public Security403Exception(IBaseEnum baseEnum) {
		this(baseEnum.reasonPhrase());
		this.baseEnum = baseEnum;
	}

	public Security403Exception(IBaseEnum baseEnum, String message) {
		this(message);
		this.baseEnum = baseEnum;
	}
	
	public Security403Exception(IBaseEnum baseEnum, Throwable cause) {
		super(cause);
		this.baseEnum = baseEnum;
	}
	
	public IBaseEnum getBaseEnum() {
		return baseEnum;
	}
	
}
