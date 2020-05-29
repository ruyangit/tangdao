/**
 *
 */
package com.tangdao.core.code;

import org.apache.commons.lang3.ArrayUtils;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
@Component
public class ControllerMethodsCache {

	@Value("${server.servlet.contextPath:}")
	private String contextPath;

	private ConcurrentMap<String, Method> methods = new ConcurrentHashMap<>();

	public ConcurrentMap<String, Method> getMethods() {
		return methods;
	}

	public Method getMethod(String httpMethod, String path) {
		String key = httpMethod + "-->" + path.replace(contextPath, "");
		return methods.get(key);
	}

	@SuppressWarnings("rawtypes")
	public void initClassMethod(String packageName) {
		Reflections reflections = new Reflections(packageName);
		Set<Class<?>> classesList = reflections.getTypesAnnotatedWith(RequestMapping.class);

		for (Class clazz : classesList) {
			initClassMethod(clazz);
		}
	}

	@SuppressWarnings("rawtypes")
	public void initClassMethod(Set<Class<?>> classesList) {
		for (Class clazz : classesList) {
			initClassMethod(clazz);
		}
	}

	public void initClassMethod(Class<?> clazz) {
		RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
		for (String classPath : requestMapping.value()) {
			for (Method method : clazz.getMethods()) {
				if (!method.isAnnotationPresent(RequestMapping.class)) {
					parseSubAnnotations(method, classPath);
					continue;
				}
				requestMapping = method.getAnnotation(RequestMapping.class);
				RequestMethod[] requestMethods = requestMapping.method();
				if (requestMethods.length == 0) {
					requestMethods = new RequestMethod[1];
					requestMethods[0] = RequestMethod.GET;
				}
				for (String methodPath : requestMapping.value()) {
					methods.put(requestMethods[0].name() + "-->" + classPath + methodPath, method);
				}
			}
		}
	}

	private void parseSubAnnotations(Method method, String classPath) {

		final GetMapping getMapping = method.getAnnotation(GetMapping.class);
		final PostMapping postMapping = method.getAnnotation(PostMapping.class);
		final PutMapping putMapping = method.getAnnotation(PutMapping.class);
		final DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
		final PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);

		if (getMapping != null) {
			put(RequestMethod.GET, classPath, getMapping.value(), method);
		}

		if (postMapping != null) {
			put(RequestMethod.POST, classPath, postMapping.value(), method);
		}

		if (putMapping != null) {
			put(RequestMethod.PUT, classPath, putMapping.value(), method);
		}

		if (deleteMapping != null) {
			put(RequestMethod.DELETE, classPath, deleteMapping.value(), method);
		}

		if (patchMapping != null) {
			put(RequestMethod.PATCH, classPath, patchMapping.value(), method);
		}

	}

	private void put(RequestMethod requestMethod, String classPath, String[] requestPaths, Method method) {
		if (ArrayUtils.isEmpty(requestPaths)) {
			methods.put(requestMethod.name() + "-->" + classPath, method);
			return;
		}
		for (String requestPath : requestPaths) {
			methods.put(requestMethod.name() + "-->" + classPath + requestPath, method);
		}
	}
}
