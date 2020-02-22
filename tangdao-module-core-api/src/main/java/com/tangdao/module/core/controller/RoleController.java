package com.tangdao.module.core.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import com.tangdao.module.core.service.IRoleService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@RestController
@RequestMapping(value = "/api/{version}/core/role", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
}
