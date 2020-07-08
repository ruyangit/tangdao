/**
 *
 */
package com.tangdao.web.controller.admin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.common.CommonResponse;
import com.tangdao.core.web.BaseController;
import com.tangdao.core.web.validate.Field;
import com.tangdao.core.web.validate.Rule;
import com.tangdao.core.web.validate.Validate;
import com.tangdao.model.domain.Policy;
import com.tangdao.modules.sys.service.PolicyService;

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
@RequestMapping(value = { "/admin/policies" })
public class PolicyController extends BaseController {

	@Autowired
	private PolicyService policyService;

	@GetMapping
	public CommonResponse page(Page<Policy> page, String PolicyName) {
		QueryWrapper<Policy> queryWrapper = new QueryWrapper<Policy>();
		if (StrUtil.isNotBlank(PolicyName)) {
			queryWrapper.like("policy_name", PolicyName);
		}
		return success(policyService.page(page, queryWrapper));
	}

	@Validate({ @Field(name = "id", rules = { @Rule(message = "查询主键不能为空") }) })
	@GetMapping("/detail")
	public CommonResponse detail(String id) {
		Policy policy = policyService.getById(id);
		Map<String, Object> data = MapUtil.newHashMap();
		data.put("policy", policy);

		return success(data);
	}

//	@Validate({ @Field(name = "policy.policyName", rules = { @Rule(message = "策略名不能为空") }) })
//	@PostMapping
//	public CommonResponse savePolicy(@RequestBody Policy policy) {
//		Policy er = policyService.findByPolicyName(policy.getPolicyName());
//		if (er != null) {
//			throw new IllegalArgumentException("策略 '" + er.getPolicyName() + "' 已存在");
//		}
//		return success(policyService.save(policy));
//	}
	
//	@PostMapping("/update")
//	public CommonResponse updatePolicy(@RequestBody PolicyDTO PolicyDto) {
//		Policy Policy = new Policy();
//		BeanUtil.copyProperties(PolicyDto, Policy);
//
//		if (!Validator.equal(PolicyDto.getPolicyName(), PolicyDto.getOldPolicyName())
//				&& PolicyService.count(Wrappers.<Policy>lambdaQuery().eq(Policy::getPolicyName, PolicyDto.getPolicyName())) > 0) {
//			throw new IllegalArgumentException("角色 '" + PolicyDto.getPolicyName() + "' 已存在");
//		}
//
//		if (PolicyService.checkUserPolicyRef(PolicyDto)) {
//			throw new BusinessException(CommonApiCode.FAIL, "操作失败，存在未解除的关联数据");
//		}
//
//		return success(PolicyService.updateById(Policy));
//	}

//	@PostMapping("/delete")
//	public CommonResponse deletePolicy(@RequestBody PolicyDTO PolicyDto) {
//		if (PolicyService.checkUserPolicyRef(PolicyDto)) {
//			throw new BusinessException(CommonApiCode.FAIL, "操作失败，存在未解除的关联数据");
//		}
//		return success(PolicyService.deletePolicy(PolicyDto.getId()));
//	}
}
