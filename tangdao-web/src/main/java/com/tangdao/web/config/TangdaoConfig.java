/**
 *
 */
package com.tangdao.web.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tangdao.core.web.aspect.DemoAspect;
import com.tangdao.web.security.user.TSessionInterceptor;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(TangdaoProperties.class)
public class TangdaoConfig implements WebMvcConfigurer {

	@Bean
	@ConditionalOnProperty(prefix = "tangdao", name = "demo", havingValue = "true", matchIfMissing = true)
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
	public TSessionInterceptor tSessionInterceptor() {
		return new TSessionInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		InterceptorRegistration irs = registry.addInterceptor(tSessionInterceptor());
		irs.order(6);
		irs.addPathPatterns("/admin/**");
		irs.excludePathPatterns("/admin/login");
	}
}
