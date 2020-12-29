/**
 *
 */
package com.tangdao.web.security.user;

import java.security.Principal;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.common.constant.CommonContext;
import com.tangdao.common.utils.WebUtils;
import com.tangdao.core.session.SessionContext;
import com.tangdao.core.session.SessionUser;
import com.tangdao.model.domain.User;
import com.tangdao.modules.sys.service.UserService;

import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月18日
 */
@Component
public class TSessionInterceptor implements HandlerInterceptor {

	public static final String REQ_ATTR_KEY_CURRENT_TSESSION = "REQ_ATTR_KEY_CURRENT_TSESSION";

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		Principal userPrincipal = request.getUserPrincipal();
		if (userPrincipal != null && (userPrincipal instanceof Authentication)) {

			UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) userPrincipal;

			SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
			SessionUser session = new SessionUser();
			session.setUsername(securityUser.getUsername());
			session.setUserId(securityUser.getId());

			if (StrUtil.equals(WebUtils.optional(request, "isu", CommonContext.NO), CommonContext.YES)
					&& StrUtil.isNotEmpty(securityUser.getId())) {
				session.setClaims(
						userService.getMap(Wrappers.<User>lambdaQuery().eq(User::getId, securityUser.getId())));
			}

			SessionContext.setSession(session);
			request.setAttribute(REQ_ATTR_KEY_CURRENT_TSESSION, session);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) throws Exception {
		SessionContext.removeSession();
	}
}
