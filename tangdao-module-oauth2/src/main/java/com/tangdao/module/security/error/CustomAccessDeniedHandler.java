///**
// * 
// */
//package com.tangdao.module.security.error;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tangdao.framework.protocol.Result;
//
///**
// * <p>
// * TODO 描述
// * </p>
// *
// * @author ruyangit@gmail.com
// * @since 2020年2月22日
// */
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//
//	private final HttpMessageConverter<String> messageConverter;
//
//	private final ObjectMapper mapper;
//
//	public CustomAccessDeniedHandler() {
//		this.mapper = new ObjectMapper();
//		this.messageConverter = new StringHttpMessageConverter();
//	}
//
//	@Override
//	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
//			AccessDeniedException ex) throws IOException {
//
////		ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
////
////		try {
////			ApiError apiError = new ApiError(FORBIDDEN, ex.getMessage(),
////					"Acesso não autorizado, favor contatar o administrador do sistema.");
////			
////			Result.createResult().fail(message)
////
////			outputMessage.setStatusCode(HttpStatus.FORBIDDEN);
////
////			messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
////
////		} catch (Exception e) {
////			log.error("Error method handle in class CustomAccessDeniedHandler: ", e);
////		} finally {
////			outputMessage.close();
////		}
//
//	}
//
//}
