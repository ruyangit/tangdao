/**
 *
 */
package com.tangdao.core.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.CommonContext.CommonApiCode;
import com.tangdao.common.exception.BusinessException;
import com.tangdao.common.exception.Security401Exception;
import com.tangdao.common.exception.Security403Exception;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月23日
 */
@Slf4j
public class AbstractExceptionHandler {

	@Autowired(required = false)
	protected TaskExecutor taskExecutor;

	/**
	 * TODO 自定义异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CommonResponse businessException(BusinessException e) {
		log.debug("自定义异常 ==> {}", e.getMessage());
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		return commonResponse.fail(e.getBaseEnum()).fail(e.getMessage());
	}

	/**
	 * TODO 参数异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { 
			IllegalArgumentException.class, 
			MethodArgumentNotValidException.class, 
			HttpMessageNotReadableException.class, 
			ServletRequestBindingException.class })
	@ResponseStatus(HttpStatus.OK)
	public @ResponseBody CommonResponse argumentException(Exception e) {
		log.debug("参数异常 ==> {}", e.getMessage());
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		return commonResponse.fail(CommonApiCode.InvalidParameter).fail(e.getMessage());
	}

	/**
	 * TODO 请求异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { HttpRequestMethodNotSupportedException.class })
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public @ResponseBody CommonResponse methodNotSupportedException(Exception e) {
		log.debug("格式异常 ==> {}", e.getMessage());
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		return commonResponse.fail(CommonApiCode.UnsupportedProtocol).fail(e.getMessage());
	}

	/**
	 * TODO 认证失败
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { Security401Exception.class })
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	public @ResponseBody CommonResponse security401Exception(Exception ex) {
		log.debug("认证失败 ==> {}", ex.getMessage());
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		if(ex instanceof Security401Exception) {
			return commonResponse.fail(((Security401Exception)ex).getBaseEnum()).fail(ex.getMessage());
		}
		return commonResponse.fail(CommonApiCode.AuthFailure_UnauthorizedOperation).fail(ex.getMessage());
	}
	
	/**
	 * TODO 暂无权限
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(value = { Security403Exception.class })
	@ResponseStatus(value = HttpStatus.FORBIDDEN)
	public @ResponseBody CommonResponse security403Exception(Exception ex) {
		log.debug("暂无权限 ==> {}", ex.getMessage());
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		if(ex instanceof Security403Exception) {
			return commonResponse.fail(((Security403Exception)ex).getBaseEnum()).fail(ex.getMessage());
		}
		return commonResponse.fail(CommonApiCode.AuthFailure_UnauthorizedOperation).fail(ex.getMessage());
	}

	/**
	 * TODO 保存全局异常信息
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public @ResponseBody CommonResponse exception(Exception e) {
		log.error("保存全局异常信息 ==> {}", e.getMessage(), e);
		taskExecutor.execute(() -> {
			// 写入到数据库
		});
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		return commonResponse.fail(CommonApiCode.InternalError).fail(e.getMessage());
	}
}
