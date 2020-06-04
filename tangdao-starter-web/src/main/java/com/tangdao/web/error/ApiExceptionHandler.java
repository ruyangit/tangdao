/**
 *
 */
package com.tangdao.web.error;

import java.util.Objects;

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.ErrorApiCode;
import com.tangdao.common.exception.BusinessException;
import com.tangdao.core.auth.AccessException;

import cn.hutool.core.exceptions.ExceptionUtil;

/**
 * <p>
 * TODO 描述 全局异常捕获
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年3月31日
 */
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(AccessException.class)
	public @ResponseBody Object accessException(AccessException e) {
		return CommonResponse.createCommonResponse().fail(e.getErrorCode());
	}
	
	@ExceptionHandler(BusinessException.class)
	public @ResponseBody Object businessException(BusinessException e) {
		return CommonResponse.createCommonResponse().fail(e.getErrorCode());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	private @ResponseBody Object handleIllegalArgumentException(IllegalArgumentException e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse().fail(ErrorApiCode.InvalidParameterValue);
		commonResponse.fail(e.getMessage());
		return commonResponse;
	}

	@ExceptionHandler(Exception.class)
	private @ResponseBody Object handleException(Exception e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse().fail(ErrorApiCode.InternalError);
		if (Objects.equals(MissingServletRequestParameterException.class, e.getClass())) {
			commonResponse.fail(ErrorApiCode.InvalidParameter);
        } else if (Objects.equals(HttpRequestMethodNotSupportedException.class, e.getClass())) {
			commonResponse.fail(ErrorApiCode.MethodNotSupported);
        }
		commonResponse.put("error_message", ExceptionUtil.getMessage(e));
		return commonResponse;
	}
}
