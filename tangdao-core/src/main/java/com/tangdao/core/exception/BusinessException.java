package com.tangdao.core.exception;

import com.tangdao.core.constant.ErrorCode;

import cn.hutool.core.exceptions.StatefulException;

/** 
 * @ClassName: BusinessException.java 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author ruyang
 * @date 2018年10月11日 下午4:26:18
 *  
 */
public class BusinessException extends StatefulException {
	
	private static final long serialVersionUID = 1L;
	
	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getCode(), errorCode.getMessage());
	}
}

