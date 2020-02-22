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
import com.tangdao.module.core.entity.Tenant;
import com.tangdao.module.core.service.ITenantService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 租户表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class TenantController extends BaseController {

	@Autowired
	private ITenantService tenantService;
	
	/**
	 * 租户表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/tenant")
	public IPage<Tenant> page(IPage<Tenant> page) {
		return tenantService.page(page);
	}

	/**
	 * 租户表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/tenants")
	public List<Tenant> list() {
		return tenantService.list();
	}

	/**
	 * 租户表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/tenant/{id}")
	public Tenant getTenant(String id) {
		return tenantService.getById(id);
	}

	/**
	 * 租户表保存
	 * 
	 * @param tenant
	 * @return
	 */
	@PostMapping("/tenant")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Tenant tenant) {
		return tenantService.save(tenant);
	}

	/**
	 * 租户表修改
	 * 
	 * @param id
	 * @param tenant
	 * @return
	 */
	@PutMapping("/tenant")
	public boolean update(@RequestBody @Validated Tenant tenant) {
		return tenantService.saveOrUpdate(tenant);
	}

	/**
	 * 租户表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/tenant/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return tenantService.removeById(id);
	}
}
