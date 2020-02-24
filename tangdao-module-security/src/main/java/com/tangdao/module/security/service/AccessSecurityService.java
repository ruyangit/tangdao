/**
 * 
 */
package com.tangdao.module.security.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.tangdao.framework.annotation.Authorize;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class AccessSecurityService {

	/**
	 * 日志服务
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 地址服务
	 */
	@Autowired
	private RequestMappingHandlerMapping mapping;
	
	/**
	 * rbac 权限校验
	 * 
	 * @param request
	 * @param authentication
	 * @return
	 */
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		if (authentication == null) {
			logger.error("请求认证失败");
			return false;
		}
		HandlerMethod handlerMethod = getHandlerMethod(request);
		if (handlerMethod == null) {
			logger.error("请求资源不存在 {}", request.getServletPath());
			return false;
		}

		// 权限编码
		Set<String> roles = new LinkedHashSet<>();
		Set<String> permissions = new LinkedHashSet<>();
		Set<String> users = new LinkedHashSet<>();
		Authorize methodAuth = handlerMethod.getMethodAnnotation(Authorize.class);
		Authorize classAuth = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Authorize.class);
		if ((methodAuth == null || methodAuth.merge()) && classAuth != null) {
			// 跳过权限校验
			if (classAuth.ignore()) {
				return true;
			}
			roles.addAll(Arrays.asList(classAuth.role()));
			permissions.addAll(Arrays.asList(classAuth.permission()));
			users.addAll(Arrays.asList(classAuth.user()));
		}
		if (methodAuth != null) {
			// 跳过权限校验
			if (methodAuth.ignore()) {
				return true;
			}
			roles.addAll(Arrays.asList(methodAuth.role()));
			permissions.addAll(Arrays.asList(methodAuth.permission()));
			users.addAll(Arrays.asList(methodAuth.user()));
		}
		
		// 在这里与数据库或者缓存中的用户权限做认证
		System.out.println(roles);
		System.out.println(permissions);
		System.out.println(users);
		System.out.println(authentication.getAuthorities());
		System.out.println(authentication.getName());
		System.out.println(CollUtil.contains(users, authentication.getName()));
		return false;
	}

	/**
	 * 获取到请求的方法
	 * 
	 * @param request
	 * @return
	 */
	private HandlerMethod getHandlerMethod(HttpServletRequest request) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
		Iterator<RequestMappingInfo> iters = handlerMethods.keySet().iterator();
		while (iters.hasNext()) {
			RequestMappingInfo rmi = iters.next();
			Iterator<String> url = rmi.getPatternsCondition().getPatterns().iterator();
			while (url.hasNext()) {
				AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(url.next());
				if (antPathMatcher.matches(request)) {
					if (rmi.getMethodsCondition().getMethods().stream().map(Enum::toString).collect(Collectors.toList())
							.contains(request.getMethod())) {
						return handlerMethods.get(rmi);
					}
				}
			}
		}
		return null;
	}
}
