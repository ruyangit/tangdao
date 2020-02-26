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
import com.tangdao.module.core.entity.Assertion;
import com.tangdao.module.core.service.IAssertionService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 断言表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class AssertionController extends BaseController {

	@Autowired
	private IAssertionService assertionService;
	
	/**
	 * 断言表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/assertions")
	public IPage<Assertion> lists(IPage<Assertion> page) {
		return assertionService.page(page);
	}

	/**
	 * 断言表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/assertion")
	public List<Assertion> list() {
		return assertionService.list();
	}

	/**
	 * 断言表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/assertion/{id}")
	public Assertion getAssertion(String id) {
		return assertionService.getById(id);
	}

	/**
	 * 断言表保存
	 * 
	 * @param assertion
	 * @return
	 */
	@PostMapping("/assertion")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Assertion assertion) {
		return assertionService.save(assertion);
	}

	/**
	 * 断言表修改
	 * 
	 * @param id
	 * @param assertion
	 * @return
	 */
	@PutMapping("/assertion")
	public boolean update(@RequestBody @Validated Assertion assertion) {
		return assertionService.saveOrUpdate(assertion);
	}

	/**
	 * 断言表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/assertion/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return assertionService.removeById(id);
	}
}
