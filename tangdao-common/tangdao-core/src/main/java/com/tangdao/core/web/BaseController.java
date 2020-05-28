package com.tangdao.core.web;

import com.tangdao.common.CommonResponse;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月2日
 */
public abstract class BaseController {

	/**
	 * 成功消息
	 * @param message
	 * @return {success:'true'}
	 */
	protected CommonResponse success(String message) {
		return CommonResponse.createCommonResponse().success(message);
	}
	
	/**
	 * 成功消息
	 * @param message
	 * @param data
	 * @return {success:'true',message:'', data:{}}
	 */
	protected CommonResponse success(String message, Object data) {
		return CommonResponse.createCommonResponse().success(message).setData(data);
	}
	
	/**
	 * 失败消息
	 * @param message
	 * @return {success:'false',message:''}
	 */
	protected CommonResponse fail(String message) {
		return CommonResponse.createCommonResponse().fail(message);
	}
}