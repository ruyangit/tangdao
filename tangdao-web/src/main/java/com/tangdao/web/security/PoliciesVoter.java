/**
 *
 */
package com.tangdao.web.security;

import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.AntPathMatcher;

import com.tangdao.model.vo.Statement;
import com.tangdao.modules.sys.service.PolicyService;
import com.tangdao.web.security.user.SecurityUser;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月6日
 */
public class PoliciesVoter implements AccessDecisionVoter<Object> {
	
	private final AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	private PolicyService policyService;
	
	public PoliciesVoter(PolicyService policyService) {
		this.policyService = policyService;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		if (authentication == null) {
			return ACCESS_DENIED;
		}
		
		if(authentication instanceof AnonymousAuthenticationToken) {
			return ACCESS_ABSTAIN;
		}
		
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		// 获取请求所需的权限
		StringBuffer sbf = new StringBuffer();
		sbf.append(request.getRequestURI().replaceAll("/", ":"));
		sbf.append(":").append(request.getMethod());
		
		String ACCESS_GRANTED_STR = "1";
		// 1、获取用户策略
		// 2、根据用户请求匹配安全策略
		// 3、校验策略拒绝或通过，默认为放弃
		Iterator<Statement> iters = policyService.getStatementSets(((SecurityUser)authentication.getPrincipal()).getId()).iterator();
		while (iters.hasNext()) {
			Statement statement = iters.next();
			Iterator<String> itersP = statement.getPermissions().iterator();
			while (itersP.hasNext()) {
				String policy = itersP.next();
				if (antPathMatcher.match(policy, sbf.substring(1)) && ACCESS_GRANTED_STR.equals(statement.getEffect())) {
					return ACCESS_GRANTED;
				}
			}
		}
		return ACCESS_DENIED;
	}

}
