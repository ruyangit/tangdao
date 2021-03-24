package com.tangdao.core.web;

import com.tangdao.core.CommonResponse;

/**
 * 
 * <p>
 * TODO 抽象
 * </p>
 *
 * @author ruyang
 * @since 2021年3月24日
 */
public abstract class BaseController {

	/**
	 * 
	 * TODO
	 * 
	 * @param result
	 * @param message
	 * @return
	 */
	protected CommonResponse renderResult(Boolean result, String message) {
		return renderResult(result, message, null);
	}

	/**
	 * 
	 * TODO
	 * 
	 * @param result
	 * @param message
	 * @param data
	 * @return
	 */
	protected CommonResponse renderResult(Boolean result, String message, Object data) {
		CommonResponse commonResponse = renderResult(data);
		commonResponse.putData("result", result);
		commonResponse.message(message);
		return commonResponse;
	}

	/**
	 * 
	 * TODO
	 * 
	 * @param data
	 * @return
	 */
	protected CommonResponse renderResult(Object data) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.setData(data);
		return commonResponse;
	}
}