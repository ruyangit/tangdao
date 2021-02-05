/**
 *
 */
package com.tangdao.web.controller.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.CommonResponse;
import com.tangdao.core.web.BaseController;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@RestController
@RequestMapping(value = { "/admin" })
public class LoginController extends BaseController {

	@RequestMapping(value = { "/login", "/check_token" }, method = { RequestMethod.GET, RequestMethod.POST })
	public CommonResponse login(HttpServletResponse response, HttpServletRequest request) {
		return null;
	}

}
