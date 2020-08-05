/**
 *
 */
package com.tangdao.web.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.Menu;
import com.tangdao.modules.sys.service.MenuService;

import cn.hutool.core.map.MapUtil;

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
	
	@Validate({ @Field(name = "id", rules = { @Rule(message = "查询主键不能为空") }) })
	@GetMapping("/detail")
	public CommonResponse detail(String id) {
		Menu menu = menuService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("menu", menu);
		
		return success(data);
	}
	
	@Validate({ @Field(name = "menu.menuName", rules = { @Rule(message = "菜单名不能为空") }) })
	@PostMapping
	public CommonResponse saveMenu(@RequestBody Menu menu) {
		return success(menuService.saveOrUpdate(menu));
	}
}
