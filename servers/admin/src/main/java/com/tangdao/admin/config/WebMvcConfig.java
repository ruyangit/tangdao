/**
 *
 */
package com.tangdao.admin.config;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tangdao.admin.web.security.resolver.LoginUserArgumentResolver;
import com.tangdao.core.web.SpringUtils;
import com.tangdao.core.web.aspect.DemoAspect;

/**
 * <p>
 * TODO MVC 配置
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Configuration
@EnableCaching
public class WebMvcConfig implements WebMvcConfigurer {

	@Bean
	@ConditionalOnProperty(prefix = "admin", name = "demo", havingValue = "true", matchIfMissing = true)
	public DemoAspect demoAspect() {
		return new DemoAspect();
	}

	@Bean
	public SpringUtils springUtils() {
		return new SpringUtils();
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

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(new LoginUserArgumentResolver());
	}

}
