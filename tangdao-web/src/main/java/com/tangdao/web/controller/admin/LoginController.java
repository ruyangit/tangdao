/**
 *
 */
package com.tangdao.web.controller.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.session.SessionContext;
import com.tangdao.core.web.BaseController;
import com.tangdao.model.domain.Menu;
import com.tangdao.model.vo.MenuVo;
import com.tangdao.modules.sys.service.MenuService;
import com.tangdao.web.security.AuthConfig;
import com.tangdao.web.security.AuthManager;
import com.tangdao.web.security.user.SecurityUser;

import cn.hutool.core.map.MapUtil;

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

	@Autowired
	private AuthManager authManager;

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = { "/login", "/check_token" }, method = { RequestMethod.GET, RequestMethod.POST })
	public CommonResponse login(HttpServletResponse response, HttpServletRequest request) {
		SecurityUser user = authManager.login(request);

		response.addHeader(AuthConfig.AUTHORIZATION_HEADER, AuthConfig.TOKEN_PREFIX + user.getToken());

		JSONObject result = new JSONObject();
		result.put(AuthConfig.ACCESS_TOKEN, user.getToken());
		result.put("username", user.getUsername());
		return success(result);
	}

	@GetMapping("/authority")
	public CommonResponse authority() {
		List<Menu> sourceList = menuService.findUserMenuList(SessionContext.getUserId());
		List<MenuVo> menuVoList = menuService.findUserMenuVoList(sourceList, false);
		List<String> permVoList = menuService.findUserPermVoList(sourceList);

		Map<String, Object> data = MapUtil.newHashMap();
		data.put("menuList", menuVoList);
		data.put("permList", permVoList);
		return CommonResponse.createCommonResponse(data);
	}

}
