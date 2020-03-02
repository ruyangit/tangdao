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
import java.util.Set;
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

import com.google.common.collect.Sets;
import com.tangdao.common.mapper.JsonMapper;
import com.tangdao.common.utils.StringUtils;
import com.tangdao.framework.annotation.Authorize;
import com.tangdao.module.security.model.Assertion;
import com.tangdao.module.security.model.AssertionEffect;
import com.tangdao.module.security.model.Request;
import com.tangdao.module.security.model.UserPrincipal;
import com.tangdao.module.security.model.condition.Condition;
import com.tangdao.module.security.model.condition.ConditionProcessHolder;

import cn.hutool.core.bean.BeanUtil;
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
	
	enum AccessStatus {
        ALLOWED,
        DENIED
    }

	/**
	 * 地址服务
	 */
	@Autowired
	private RequestMappingHandlerMapping mapping;

	@Autowired
	private ConditionProcessHolder conditionProcessHolder;

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

		// 如果有条件，则是强制判断
		Map<String, Map<String, Object>> conditions = new HashMap<String, Map<String, Object>>();

		Map<String, Object> reqs = new HashMap<String, Object>();
		List<String> ipAddress = new ArrayList<String>();
		ipAddress.add("192.168.0.0/16");
		ipAddress.add("192.168.56.1");
		reqs.put("iam:SourceIp", ipAddress);

		conditions.put("IpAddress", reqs);

		assertion.setCondition(JsonMapper.toJson(conditions));

		// role
		if (CollUtil.isNotEmpty(Arrays.asList(authorize.role()))) {
			assertion.setRole(JsonMapper.toJson(authorize.role()));
		}
		
		Assertion assertion2 = new Assertion();
		BeanUtil.copyProperties(assertion, assertion2);
		assertion2.setEffect(AssertionEffect.DENY);
		
		Map<String, Map<String, Object>> conditions2 = new HashMap<String, Map<String, Object>>();
		
		Map<String, Object> reqs2 = new HashMap<String, Object>();
		reqs2.put("iam:Username", "system");

		conditions2.put("StringEquals", reqs2);
		assertion2.setCondition(JsonMapper.toJson(conditions2));
		
		// 模拟策略配置结束

		// 请求的资源匹配的条件
		Request req = new Request();
		req.setAction("core:user:ListUser");
		req.setResource("*");

		Object principal = authentication.getPrincipal();
		if (principal != null && principal instanceof UserPrincipal) {
			req.setPrincipal(((UserPrincipal) principal));
		}

		// req context
		req.addContext("iam:SourceIp", "192.168.0.10");
//		req.addContext("iam:Username", "true");
		
		
		List<Assertion> assertions = new ArrayList<Assertion>();

		assertions.add(assertion);
		assertions.add(assertion2);
		
		AccessStatus accessStatus = AccessStatus.DENIED;
		
		for (Assertion ass : assertions) {
			System.out.println(JsonMapper.toJson(ass));
			AssertionEffect effect = ass.effect;
			if (effect == null) {
                effect = AssertionEffect.ALLOW;
            }
			if (accessStatus == AccessStatus.ALLOWED && effect == AssertionEffect.ALLOW) {
                continue;
            }
			if (!assertionMatch(ass, req)) {
				continue;
			}
			if (effect == AssertionEffect.DENY) {
                return false;
            }
            accessStatus = AccessStatus.ALLOWED;
		}
		return accessStatus == AccessStatus.ALLOWED;
	}

	/**
	 * 断言
	 * 
	 * @param assertion
	 * @param request
	 * @return
	 */
	private boolean assertionMatch(Assertion assertion, Request request) {

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
		
		if (null != assertion.getCondition() && !(matchResult = this.evaluateCondition(assertion.getCondition(), request))) {
			return false;
		}

		logger.debug("AssertionMatch: -> " + matchResult + " (effect: " + assertion.getEffect() + ")");
		return matchResult;
	}

	// Validate Authorize role
	private boolean matchPrincipal(String role, Request request) {
		try {
			if (StrUtil.isBlank(role)) {
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
	@SuppressWarnings("unchecked")
	private boolean evaluateCondition(String condition, Request request) {
		if (StrUtil.isEmpty(condition)) {
			return true;
		}
		// 条件集合
		Map<String, Map<String, Object>> conditions = JsonMapper.fromJson(condition, Map.class);
		// 每一个 操作符 and
		return conditions.entrySet().stream().allMatch(item -> {
			String key = item.getKey(); // 条件操作符

			// 子条件操作 ifexists 上下文中没有获取的键值的情况 返回 true
			// 子条件操作 for any 有一项生效 返回true
			// 子条件操作 for all 所有项生效 返回true
			// 获取键值数据
			if (key.startsWith(Condition.FOR_ANY_VALUE_PREFIX) || key.startsWith(Condition.FOR_ALL_VALUES_PREFIX)) {
				key = key.split(":")[0];
			} else if (key.endsWith(Condition.IF_EXISTS_SUFFIX)) {
				key = key.substring(0, (key.length() - Condition.IF_EXISTS_SUFFIX.length() - 1));
			}

			// process
			Condition process = conditionProcessHolder.getCondition(key);
			if (null == process) {
				return false;
			}

			// 每一个 键 and
			return item.getValue().entrySet().stream().allMatch(e -> {
				Set<String> keys = Sets.newHashSet();
				Set<String> values = Sets.newHashSet();
				// 从上下文中获取
				Object obj = request.getContext().get(e.getKey());
				if (null == obj) {
					return false;
				}
				if (obj instanceof List) {
					((List<String>) obj).stream().forEach(r -> keys.add(r));
				} else {
					keys.add(String.valueOf(obj));
				}
				if (e.getValue() instanceof List) {
					((List<String>) e.getValue()).stream().forEach(r -> values.add(r));
				} else {
					values.add(String.valueOf(e.getValue()));
				}
				boolean isExists = item.getKey().endsWith(Condition.IF_EXISTS_SUFFIX);
				if (item.getKey().startsWith(Condition.FOR_ALL_VALUES_PREFIX)) {
					return process.evaluateForAllValue(keys, values, isExists);
				}
				return process.evaluateForAnyValue(keys, values, isExists);
			});
		});
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
