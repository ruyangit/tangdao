/**
 *
 */
package com.tangdao.core.auth;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.tangdao.common.utils.WebUtils;
import com.tangdao.core.code.ControllerMethodsCache;

import cn.hutool.core.exceptions.ExceptionUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public class AuthFilter implements Filter {

	private static final String SERVER_HEADER = "X-Server";

	@Autowired
	private AuthManager authManager;

	@Autowired
	private ControllerMethodsCache methodsCache;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String userAgent = WebUtils.getUserAgent(req);

		if (StringUtils.startsWith(userAgent, SERVER_HEADER)) {
			chain.doFilter(request, response);
			return;
		}

		try {

			String path = new URI(req.getRequestURI()).getPath();
			Method method = methodsCache.getMethod(req.getMethod(), path);

			if (method == null) {
				chain.doFilter(request, response);
				return;
			}

			if (method.isAnnotationPresent(Secured.class)) {

				Secured secured = method.getAnnotation(Secured.class);
				String action = secured.action().toString();
				String resource = secured.resource();

//                if (StringUtils.isBlank(resource)) {
//                    ResourceParser parser = secured.parser().newInstance();
//                    resource = parser.parseName(req);
//                }
//
//                if (StringUtils.isBlank(resource)) {
//                    // deny if we don't find any resource:
//                    throw new AccessException("resource name invalid!");
//                }

				authManager.auth(new Permission(resource, action), authManager.login(req));

			}
			chain.doFilter(request, response);
		} catch (AccessException e) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		} catch (IllegalArgumentException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, ExceptionUtil.getRootCauseMessage(e));
			return;
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server failed," + e.getMessage());
			return;
		}

	}

}
