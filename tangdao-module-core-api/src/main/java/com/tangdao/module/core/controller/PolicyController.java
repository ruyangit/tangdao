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
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.framework.web.BaseController;
import com.tangdao.module.core.model.domain.Policy;
import com.tangdao.module.core.service.IPolicyService;

/**
 * <p>
 * 策略 前端控制器
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@RestController
@RequestMapping(value = "/{env}/policies", produces = MediaType.APPLICATION_JSON_VALUE)
public class PolicyController extends BaseController {

	@Autowired
	private IPolicyService policyService;
	
	/**
	 * 策略分页
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping
	public IPage<Policy> page(Page<Policy> page) {
		return policyService.page(page);
	}

	/**
	 * 策略列
	 * 
	 * @param page
	 * @return
	 */
	@GetMapping("/policy")
	public List<Policy> list() {
		return policyService.list();
	}

	/**
	 * 策略信息
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{policyId}")
	public Policy get(@PathVariable("policyId") String policyId) {
		return policyService.getById(policyId);
	}

	/**
	 * 策略保存
	 * 
	 * @param policy
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public boolean save(@RequestBody @Validated Policy policy) {
		return policyService.save(policy);
	}

	/**
	 * 策略修改
	 * 
	 * @param id
	 * @param policy
	 * @return
	 */
	@PutMapping("/{policyId}")
	public boolean update(@PathVariable("policyId") String policyId, @RequestBody @Validated Policy policy) {
		return policyService.saveOrUpdate(policy);
	}

	/**
	 * 策略删除
	 * 
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{policyId}")
	public boolean delete(@PathVariable("policyId") String policyId) {
		return policyService.removeById(policyId);
	}
}
