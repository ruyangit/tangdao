package com.tangdao.framework.exception;

import com.tangdao.framework.protocol.IEnum;

/**
 * @ClassName: BusinessException.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ruyang
 * @date 2018年10月11日 下午4:26:18
 * 
 */
public class BusinessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String status;

	public BusinessException() {
		super();
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(String status, String message) {
		super(message);
		this.status = status;
	}

	public BusinessException(String status, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.status = status;
	}

	public BusinessException(IEnum<String, String> status, String msgFormat, Object... args) {
		super(String.format(msgFormat, args));
		this.status = status.value();
	}

	public String getStatus() {
		return status;
	}
	
}
