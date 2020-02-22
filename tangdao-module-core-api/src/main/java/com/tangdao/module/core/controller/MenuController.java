package com.tangdao.module.core.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangdao.module.core.model.domain.Menu;
import com.tangdao.module.core.service.IMenuService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuController extends BaseController {

	@Autowired
	private IMenuService menuService;
	
	/**
	 * 菜单表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/menu")
	public IPage<Menu> page(IPage<Menu> page) {
		return menuService.page(page);
	}

	/**
	 * 菜单表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/menus")
	public List<Menu> list() {
		return menuService.list();
	}

	/**
	 * 菜单表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/menu/{id}")
	public Menu getMenu(String id) {
		return menuService.getById(id);
	}

	/**
	 * 菜单表保存
	 * 
	 * @param menu
	 * @return
	 */
	@PostMapping("/menu")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Menu menu) {
		return menuService.save(menu);
	}

	/**
	 * 菜单表修改
	 * 
	 * @param id
	 * @param menu
	 * @return
	 */
	@PutMapping("/menu")
	public boolean update(@RequestBody @Validated Menu menu) {
		return menuService.saveOrUpdate(menu);
	}

	/**
	 * 菜单表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/menu/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return menuService.removeById(id);
	}
}
