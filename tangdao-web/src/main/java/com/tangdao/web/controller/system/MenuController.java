/**
 *
 */
package com.tangdao.web.controller.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.web.BaseController;
import com.tangdao.system.service.MenuService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月23日
 */
@RestController
@RequestMapping(value = { "v1/system" })
public class MenuController extends BaseController {

	@Autowired
	private MenuService menuService;

//	@GetMapping("/menu-tree")
//	public CommonResponse tree() {
//		return success(menuService.findMenuVoChildList());
//	}
//	
//	@Validate({ @Field(name = "id", rules = { @Rule(message = "查询主键不能为空") }) })
//	@GetMapping("/menu-detail")
//	public CommonResponse detail(String id) {
//		Menu menu = menuService.getById(id);
//		Map<String, Object> data = MapUtil.newHashMap();
//		data.put("menu", menu);
//		
//		return success(data);
//	}
//	
//	@Validate({ @Field(name = "menu.menuName", rules = { @Rule(message = "菜单名不能为空") }) })
//	@PostMapping("/menus")
//	public CommonResponse saveMenu(@RequestBody Menu menu) {
//		return success(menuService.saveOrUpdate(menu));
//	}
}
