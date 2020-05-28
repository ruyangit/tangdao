package com.tangdao.common.exception;

import com.tangdao.common.constant.IBaseEnum;

/** 
 * @ClassName: BusinessException.java 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ruyang
 * @date 2018年10月11日 下午4:26:18
 *  
 */
public class BusinessException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public BusinessException() {
		
	}
	
	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
	
	private IBaseEnum baseEnum;
	
	public BusinessException(IBaseEnum baseEnum) {
		this(baseEnum.reasonPhrase());
		this.baseEnum = baseEnum;
	}

	public BusinessException(IBaseEnum baseEnum, String message) {
		this(message);
		this.baseEnum = baseEnum;
	}
	
	public BusinessException(IBaseEnum baseEnum, Throwable cause) {
		super(cause);
		this.baseEnum = baseEnum;
	}
	
	public IBaseEnum getBaseEnum() {
		return baseEnum;
	}
}

