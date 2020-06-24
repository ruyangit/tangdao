/**
 *
 */
package com.tangdao.web.error;

import java.util.Objects;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.CommonApiCode;
import com.tangdao.common.exception.BusinessException;

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
public class WebExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public @ResponseBody Object businessException(BusinessException e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(e.getErrorCode().getCode(), e.getMessage());
		return commonResponse;
	}

	@ExceptionHandler(IllegalArgumentException.class)
	private @ResponseBody Object handleIllegalArgumentException(IllegalArgumentException e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(CommonApiCode.BAD_REQUEST.getCode(), e.getMessage());
		return commonResponse;
	}

	@ExceptionHandler(AccessDeniedException.class)
	private @ResponseBody Object accessDeniedException(AccessDeniedException e) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(CommonApiCode.FORBIDDEN.getCode(), e.getMessage());
		return commonResponse;
	}

	@ExceptionHandler(Exception.class)
	private @ResponseBody Object handleException(Exception e) {
		e.printStackTrace();
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
		if (Objects.equals(MissingServletRequestParameterException.class, e.getClass())) {
			commonResponse.fail(CommonApiCode.BAD_REQUEST);
		}
		// 异常描述
		commonResponse.put("message_description", ExceptionUtil.getMessage(e));
		return commonResponse;
	}
}
