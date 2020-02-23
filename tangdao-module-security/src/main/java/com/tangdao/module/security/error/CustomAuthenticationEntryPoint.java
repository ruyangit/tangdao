///**
// * 
// */
//package com.tangdao.module.security.error;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//
///**
// * <p>
// * TODO 描述
// * </p>
// *
// * @author ruyangit@gmail.com
// * @since 2020年2月22日
// */
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//
//	private final HttpMessageConverter<String> messageConverter;
//
//    private final ObjectMapper mapper;
//
//    public CustomAuthenticationEntryPoint(ObjectMapper mapper) {
//        this.messageConverter = new StringHttpMessageConverter();
//        this.mapper = mapper;
//    }
//
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException ex) throws IOException {
//        
//    	ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);
//    	
//    	try {
//    		ApiError apiError = new ApiError(UNAUTHORIZED, ex.getMessage(), ex);
//    		
//    		outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);
//    		
//    		messageConverter.write(mapper.writeValueAsString(apiError), MediaType.APPLICATION_JSON, outputMessage);
//			
//		} catch (Exception e) {
//			log.error("Error method commence in class CustomAuthenticationEntryPoint: ", e);
//		} finally {
//			outputMessage.close();
//		}
//    }
//
//}
