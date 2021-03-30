/**
 *
 */
package com.tangdao.config;

import java.util.List;

import javax.servlet.Filter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tangdao.core.web.aspect.DemoAspect;
import com.tangdao.core.web.filter.BodyReaderFilter;
import com.tangdao.core.web.interceptor.LogInterceptor;
import com.tangdao.web.security.resolver.LoginUserArgumentResolver;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月24日
 */
@Configuration
@EnableCaching
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	@ConditionalOnProperty(prefix = "tangdao-web", name = "demo", havingValue = "true", matchIfMissing = true)
	public DemoAspect demoAspect() {
		return new DemoAspect();
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.setMaxAge(18000L);
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		return new CorsFilter(source);
	}

	@Bean
	public FilterRegistrationBean<Filter> filters() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<Filter>();
		registrationBean.setFilter(new BodyReaderFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginUserArgumentResolver());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LogInterceptor()).excludePathPatterns("/api/**").addPathPatterns("/**");
	}

}
