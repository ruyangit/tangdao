/**
 *
 */
package com.tangdao.admin.web.error;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tangdao.core.CommonResponse;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.exception.BusinessException;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * TODO 描述 全局异常捕获
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年3月31日
 */
@RestControllerAdvice
public class WebExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public @ResponseBody Object businessException(BusinessException e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(ObjectUtil.toString(e.getCode()), e.getMessage());
		return commonResponse;
	}

	@ExceptionHandler(Exception.class)
	private @ResponseBody Object handleException(Exception e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(CommonApiCode.COMMON_SERVER_EXCEPTION);
		commonResponse.put("message_description", ExceptionUtil.getMessage(e));
		return commonResponse;
	}
}
