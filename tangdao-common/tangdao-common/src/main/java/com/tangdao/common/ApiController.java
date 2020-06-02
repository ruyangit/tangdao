package com.tangdao.common;

import com.tangdao.common.constant.ErrorCode;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月2日
 */
public abstract class ApiController {

	/**
	 * 成功消息
	 * @param message
	 * @return {success:true}
	 */
	protected CommonResponse success(String message) {
		return CommonResponse.createCommonResponse().success(message);
	}
	
	/**
	 * 成功消息
	 * @param message
	 * @return {success:true}
	 */
	protected CommonResponse success(Object data) {
		return CommonResponse.createCommonResponse().success(CommonResponse.SUCCESS).setData(data);
	}
	
	/**
	 * 成功消息
	 * @param message
	 * @param data
	 * @return {success:true, message:'', data:{}}
	 */
	protected CommonResponse success(String message, Object data) {
		return CommonResponse.createCommonResponse().success(message).setData(data);
	}
	
	/**
	 * 失败消息
	 * @param message
	 * @return {success:false,message:''}
	 */
	protected CommonResponse fail(String message) {
		return CommonResponse.createCommonResponse().fail(message);
	}
	
	/**
	 * 失败消息
	 * @param message
	 * @return {success:false,message:''}
	 */
    protected CommonResponse fail(ErrorCode errorCode) {
    	return CommonResponse.createCommonResponse().fail(errorCode);
    }
}