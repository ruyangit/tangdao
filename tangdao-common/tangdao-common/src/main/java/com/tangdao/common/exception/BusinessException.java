package com.tangdao.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tangdao.common.constant.IBaseEnum;

/** 
 * @ClassName: BusinessException.java 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ruyang
 * @date 2018年10月11日 下午4:26:18
 *  
 */
@ResponseStatus(HttpStatus.OK)
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
		this(baseEnum.message());
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

