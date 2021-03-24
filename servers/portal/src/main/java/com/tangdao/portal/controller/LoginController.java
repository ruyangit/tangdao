/**
 *
 */
package com.tangdao.portal.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.portal.web.security.AuthProvider;
import com.tangdao.portal.web.security.WebSecurityConfig;
import com.tangdao.portal.web.security.model.AuthUser;

/**
 * 
 * <p>
 * TODO 登录
 * </p>
 *
 * @author ruyang
 * @since 2021年3月12日
 */
@RestController
@RequestMapping("/api")
public class LoginController extends BaseController {

	@Autowired
	private AuthProvider authProvider;

	@RequestMapping(value = { "/login", "/check_token" }, method = { RequestMethod.GET, RequestMethod.POST })
	public CommonResponse login(HttpServletRequest request, HttpServletResponse response) throws IOException {
		AuthUser user = authProvider.login(request, response);
		if (user != null) {
			response.addHeader(WebSecurityConfig.AUTHORIZATION_HEADER,
					WebSecurityConfig.TOKEN_PREFIX + user.getToken());
		}
		JSONObject data = new JSONObject();
		data.put(WebSecurityConfig.ACCESS_TOKEN, user.getToken());
		data.put("username", user.getUsername());
		return renderResult(data);
	}
}
