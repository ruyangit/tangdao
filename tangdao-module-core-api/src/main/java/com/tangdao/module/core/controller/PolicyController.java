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
import com.tangdao.module.core.entity.Policy;
import com.tangdao.module.core.service.IPolicyService;
import com.tangdao.framework.web.BaseController;

/**
 * <p>
 * 策略表 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@RestController
@RequestMapping(value = "/api/{env}/core", produces = MediaType.APPLICATION_JSON_VALUE)
public class PolicyController extends BaseController {

	@Autowired
	private IPolicyService policyService;
	
	/**
	 * 策略表分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/policys")
	public IPage<Policy> lists(IPage<Policy> page) {
		return policyService.page(page);
	}

	/**
	 * 策略表列表
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/policy")
	public List<Policy> list() {
		return policyService.list();
	}

	/**
	 * 策略表信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/policy/{id}")
	public Policy getPolicy(String id) {
		return policyService.getById(id);
	}

	/**
	 * 策略表保存
	 * 
	 * @param policy
	 * @return
	 */
	@PostMapping("/policy")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Policy policy) {
		return policyService.save(policy);
	}

	/**
	 * 策略表修改
	 * 
	 * @param id
	 * @param policy
	 * @return
	 */
	@PutMapping("/policy")
	public boolean update(@RequestBody @Validated Policy policy) {
		return policyService.saveOrUpdate(policy);
	}

	/**
	 * 策略表删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/policy/{id}")
	public boolean delete(@PathVariable("id") String id) {
		return policyService.removeById(id);
	}
}
