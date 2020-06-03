/**
 *
 */
package com.tangdao.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.common.ApiController;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.auth.AccessException;
import com.tangdao.web.security.AuthConfig;
import com.tangdao.web.security.AuthManagerImpl;
import com.tangdao.web.security.user.SecurityUser;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@RestController
@RequestMapping(value = { "/v1/auth" })
public class LoginController extends ApiController {

	@Autowired
	private AuthManagerImpl authManager;

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public CommonResponse login(HttpServletResponse response, HttpServletRequest request) throws AccessException {
		SecurityUser user = (SecurityUser) authManager.login(request);

		response.addHeader(AuthConfig.AUTHORIZATION_HEADER, AuthConfig.TOKEN_PREFIX + user.getToken());

		JSONObject result = new JSONObject();
		result.put("access_token", user.getToken());
		result.put("username", user.getUsername());
		return success(result);
	}
}
