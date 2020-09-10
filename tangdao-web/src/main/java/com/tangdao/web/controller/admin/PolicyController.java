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

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.constant.DataStatus;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.Policy;
import com.tangdao.model.dto.PolicyDTO;
import com.tangdao.modules.sys.service.PolicyService;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@RestController
@RequestMapping(value = { "/admin" })
public class PolicyController extends BaseController {

	@Autowired
	private PolicyService policyService;

	@GetMapping("/policies")
	public CommonResponse page(Page<Policy> page, String PolicyName) {
		QueryWrapper<Policy> queryWrapper = new QueryWrapper<Policy>();
		if (StrUtil.isNotBlank(PolicyName)) {
			queryWrapper.like("policy_name", PolicyName);
		}
		return success(policyService.page(page, queryWrapper));
	}
	
	@GetMapping("/policy-list")
	public CommonResponse list(String policyName) {
		QueryWrapper<Policy> queryWrapper = new QueryWrapper<Policy>();
		if (StrUtil.isNotBlank(policyName)) {
			queryWrapper.like("policy_name", policyName);
		}
		queryWrapper.eq("status", DataStatus.NORMAL);
		return success(policyService.list(queryWrapper));
	}

	@Validate({ @Field(name = "id", rules = { @Rule(message = "查询主键不能为空") }) })
	@GetMapping("/policy-detail")
	public CommonResponse detail(String id) {
		Policy policy = policyService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("policy", policy);
		
		return success(data);
	}

	@Validate({ @Field(name = "policy.policyName", rules = { @Rule(message = "策略名不能为空") }) })
	@PostMapping("/policies")
	public CommonResponse savePolicy(@RequestBody Policy policy) {
		Policy er = policyService.findByPolicyName(policy.getPolicyName());
		if (er != null) {
			throw new IllegalArgumentException("策略 '"+policy.getPolicyName()+"' 已存在");
		}
		return success(policyService.save(policy));
	}
	
	@PostMapping("/policy-update")
	public CommonResponse updatePolicy(@RequestBody PolicyDTO policyDto) {
		Policy policy = new Policy();
		BeanUtil.copyProperties(policyDto, policy);

		if (!Validator.equal(policyDto.getPolicyName(), policyDto.getOldPolicyName())
				&& policyService.count(Wrappers.<Policy>lambdaQuery().eq(Policy::getPolicyName, policyDto.getPolicyName())) > 0) {
			throw new IllegalArgumentException("策略 '"+policy.getPolicyName()+"' 已存在");
		}
		return success(policyService.updateById(policy));
	}

	@PostMapping("/policy-delete")
	public CommonResponse deletePolicy(@RequestBody PolicyDTO policyDto) {
		return success(policyService.removeById(policyDto.getId()));
	}
}
