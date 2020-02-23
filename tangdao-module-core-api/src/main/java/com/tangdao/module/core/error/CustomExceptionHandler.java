///**
// * 
// */
//package com.tangdao.module.core.error;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.ServletRequestBindingException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
//import com.tangdao.framework.exception.ParamValidationException;
//import com.tangdao.framework.exception.PermissionDeniedException;
//import com.tangdao.framework.exception.ResourceNotFoundException;
//import com.tangdao.framework.exception.ServiceException;
//import com.tangdao.framework.protocol.Result;
//
///**
// * <p>
// * TODO 异常全局捕获
// * </p>
// *
// * @author ruyangit@gmail.com
// * @since 2020年2月22日
// */
//
//@ControllerAdvice(annotations = ResponseBody.class)
//public class CustomExceptionHandler {
//
//	/**
//	 * 日志服务
//	 */
//	private static final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);
//	
//	@ResponseBody
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(value = Exception.class)
//    public Result commonExceptionHandle(Exception e) {
//        Result result = Result.createResult();
//        logger.error("[SystemException]Exception:", e);
//        return result.fail("System Error, please try again later! Message:" + e.getMessage());
//    }
//	
//    @ResponseBody
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = PermissionDeniedException.class)
//    public Result permissionDeniedExceptionHandle(Exception e) {
//        Result result = Result.createResult();
//        logger.error("[PermissionDeniedException]Exception:", e);
//        return result.fail("PermissionDeniedException, Message:" + e.getMessage());
//    }
//    
//    @ResponseBody
//    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
//    @ExceptionHandler(value = ServiceException.class)
//    public Result serviceExceptionHandle(Exception e) {
//    	Result result = Result.createResult();
//    	logger.error("[ServiceException]Exception:", e);
//    	return result.fail("ServiceException, Message:" + e.getMessage());
//    }
//
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(value = {ParamValidationException.class, ServletRequestBindingException.class})
//    public Result paramValidationExceptionHandle(Exception e) {
//        Result result = Result.createResult();
//        logger.error("[ParamValidationException]Exception:", e);
//        return result.fail("Parameter validation failure! Message:" + e.getMessage());
//    }
//
//    @ResponseBody
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @ExceptionHandler(value = {ResourceNotFoundException.class})
//    public Result resourceNotFoundExceptionHandle(Exception e) {
//        Result result = Result.createResult();
//        logger.error("[ResourceNotFoundException]Exception:", e);
//        return result.fail("Resource not found! Message:" + e.getMessage());
//    }
//    
//}
