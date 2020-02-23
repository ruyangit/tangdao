/**
 * 
 */
package com.tangdao.module.security.error;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tangdao.framework.protocol.Result;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月22日
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final HttpMessageConverter<String> messageConverter;

    private final ObjectMapper mapper;
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    public CustomAccessDeniedHandler(ObjectMapper mapper) {
        this.messageConverter = new StringHttpMessageConverter();
        this.mapper = mapper;
    }

	@Override
	public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			AccessDeniedException ex) throws IOException {

		ServerHttpResponse outputMessage = new ServletServerHttpResponse(httpServletResponse);

		try {
			Result result = Result.createResult();
    		result.fail(ex.getMessage());
    		
    		outputMessage.setStatusCode(HttpStatus.FORBIDDEN);

			messageConverter.write(mapper.writeValueAsString(result), MediaType.APPLICATION_JSON, outputMessage);

		} catch (Exception e) {
			logger.error("Error method handle in class CustomAccessDeniedHandler: ", e);
		} finally {
			outputMessage.close();
		}

	}

}
