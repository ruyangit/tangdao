/**
 * 
 */
package com.tangdao.framework.autoconfigure;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tangdao.common.mapper.JsonMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Configuration
public class CoreWebMvcAutoConfigurer implements WebMvcConfigurer {

	@Autowired
	private HttpMessageConverters httpMessageConverters;
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
		converters.add(stringConverter);
		httpMessageConverters.getConverters().forEach(converter->{
			converters.add(converter);
		});
		converters.add(new MappingJackson2HttpMessageConverter(JsonMapper.getInstance()));
	}
	
}
