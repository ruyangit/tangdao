/**
 * 
 */
package com.tangdao.module.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.tangdao.common.mapper.JsonMapper;
import com.tangdao.common.utils.StringUtils;
import com.tangdao.framework.annotation.Authorize;
import com.tangdao.module.security.model.Assertion;
import com.tangdao.module.security.model.AssertionEffect;
import com.tangdao.module.security.model.Request;
import com.tangdao.module.security.model.condition.Condition;
import com.tangdao.module.security.model.condition.ConditionHolder;
import com.tangdao.module.security.model.condition.IpAddressCondition;
import com.tangdao.module.security.service.UserPrincipal;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年2月23日
 */
@Component
public class DynamicAccessDecisionManager {

	/**
	 * 日志服务
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 地址服务
	 */
	@Autowired
	private RequestMappingHandlerMapping mapping;
	
	@Autowired
	private ConditionHolder conditionHolder;

	/**
	 * rbac pbac 权限校验
	 * 
	 * @param request
	 * @param authentication
	 * @return
	 */
	public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated()) {
			logger.error("请求认证失败");
			return false;
		}
		HandlerMethod handlerMethod = getHandlerMethod(request);
		if (handlerMethod == null) {
			logger.error("请求资源不存在 {}", request.getServletPath());
			return false;
		}

		// 权限编码
		Authorize authorize = handlerMethod.getMethodAnnotation(Authorize.class);
		if (authorize == null) {
			return true;
		}

		// 模拟策略配置开始
		List<String> action = new ArrayList<String>();
//		action.add("*");
//		action.add("*:*");
//		action.add("core:*");
//		action.add("core:user:*");
		action.add("core:user:List*");
		List<String> resource = new ArrayList<String>();
		resource.add("*");
		
		Assertion assertion = new Assertion();
		assertion.setEffect(AssertionEffect.ALLOW);
		assertion.setAction(JsonMapper.toJson(action));
		assertion.setResource(JsonMapper.toJson(resource));
		
		Map<String, Condition> conditions = new HashMap<String, Condition>();
		
		Condition reqs = new IpAddressCondition();
		List<String> ipAddress = new ArrayList<String>();
		ipAddress.add("127.0.0.1");
		ipAddress.add("192.168.56.1");
		reqs.put("iam:SourceIp", ipAddress);
		
		conditions.put("IpAddress", reqs);
		assertion.setCondition(JsonMapper.toJson(conditions));

		// role
		if (CollUtil.isNotEmpty(Arrays.asList(authorize.role()))) {
			assertion.setRole(JsonMapper.toJson(authorize.role()));
		}
		// 模拟策略配置结束

		// 请求的资源匹配的条件
		Request req = new Request();
		req.setAction("core:user:ListUser");
		req.setResource("");

		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof UserPrincipal) {
			req.setPrincipal(((UserPrincipal) principal));
		}
		
		//req context
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("IpAddress", "127.0.0.1");
		m.put("Username", "system");
		req.addContext("req", m);

		if (AssertionMatch(assertion, req)) {
			
			return true;
		}

		return false;
	}

	/**
	 * 断言
	 * @param assertion
	 * @param request
	 * @return
	 */
	private boolean AssertionMatch(Assertion assertion, Request request) {

		// effect
		if (null == assertion.getEffect()) {
			return false;
		}

		// resource
		if (null != assertion.getResource() && !this.evaluateResource(assertion.getResource(), request)) {
			return false;
		}

		// action
		if (null != assertion.getAction() && !this.evaluateAction(assertion.getAction(), request)) {
			return false;
		}

		// role
		boolean matchResult = true;
		if (null != assertion.getRole() && !(matchResult = this.matchPrincipal(assertion.getRole(), request))) {
			return false;
		}
		
		evaluateCondition(assertion.getCondition(), request);
		
		logger.debug("AssertionMatch: -> " + matchResult + " (effect: " + assertion.getEffect() + ")");
		return matchResult;
	}

	// Validate Authorize role
	private boolean matchPrincipal(String role, Request request) {
		try {
			if(StrUtil.isBlank(role)) {
				return true;
			}
			List<String> expressions = JsonMapper.fromJson(role, List.class);
			return request.getPrincipal().getAuthorities().stream().anyMatch(item -> expressions.stream()
					.anyMatch(r -> stringLike("ROLE_" + r.toUpperCase(), item.getAuthority())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Validate Resource Default *
	private boolean evaluateResource(String resource, Request request) {
		try {
			List<String> expressions = JsonMapper.fromJson(resource, List.class);
			return expressions.stream().anyMatch(r -> stringLike(r, request.getResource()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Validate Action
	private boolean evaluateAction(String action, Request request) {
		try {
			List<String> expressions = JsonMapper.fromJson(action, List.class);
			return expressions.stream().anyMatch(r -> stringLike(r, request.getAction()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// Validate Condition
	private boolean evaluateCondition(String condition, Request request) {
		if(StrUtil.isEmpty(condition)) {
			return true;
		}
		System.out.println(condition);
		Map<String, Map<String, Object>> conditions = JsonMapper.fromJson(condition, Map.class);
		for(Map.Entry<String, Map<String, Object>> entry : conditions.entrySet()) {
            String key = entry.getKey();
            Condition cond= conditionHolder.findCondition(key);
            cond.putAll(entry.getValue());
            System.out.println(cond.fullfills(key, request));
//            Condition condi = entry.getValue();
//            System.out.println(condi);
//            if(!condi.fullfills(key, request)){
//                return false;
//            }
        }
        return true;
	}

	// Validate StringLike
	private boolean stringLike(String arg1, String arg2) {
		arg1 = StringUtils.patternFromGlob(arg1);
		return arg2.matches(arg1);
	}

	// Get HandlerMethod
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
