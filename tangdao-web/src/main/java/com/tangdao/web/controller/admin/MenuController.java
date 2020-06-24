/**
 *
 */
package com.tangdao.web.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.modules.sys.service.MenuService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月23日
 */
@RestController
@RequestMapping(value = { "/admin/menus" })
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

	@GetMapping("/tree")
	public CommonResponse tree() {
		return success(menuService.findMenuVoChildList());
	}
}
