/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tangdao.core.service.BaseService;
import com.tangdao.model.domain.Policy;
import com.tangdao.model.vo.Statement;
import com.tangdao.modules.sys.mapper.PolicyMapper;

import cn.hutool.core.collection.CollUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Service
public class PolicyService extends BaseService<PolicyMapper, Policy> {

	public List<Policy> findUserPolicy(String userId) {
		return this.baseMapper.findUserPolicyList(userId);
	}

	public List<Policy> findRolePolicy(String userId) {
		return this.baseMapper.findRolePolicyList(userId);
	}
	
	public Policy findByPolicyName(String policyName) {
		return findPolicy(this.list(Wrappers.<Policy>lambdaQuery().eq(Policy::getPolicyName, policyName)));
	}

	public Policy findPolicy(List<Policy> policies) {
		if (CollUtil.isNotEmpty(policies)) {
			return policies.get(0);
		}
		return null;
	}

	@Cacheable(value = CacheService.RED_USER_POLICY_STATEMENTS, key = "#userId")
	public Set<Statement> getStatementSets(String userId) {
		List<Policy> policies = this.findUserPolicy(userId);
		if (CollUtil.isEmpty(policies)) {
			policies = CollUtil.newArrayList();
		}
		policies.addAll(this.findRolePolicy(userId));
		Set<Statement> statementSets = new HashSet<Statement>();
		policies.stream().forEach(e -> {
			List<Statement> statements = JSON.parseArray(e.getContent(), Statement.class);
			statementSets.addAll(statements);
		});
		return statementSets;
	}

}
