/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson.JSON;
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

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	public List<Policy> findUserPolicy(String userId) {
		return this.baseMapper.findUserPolicyList(userId);
	}

	public List<Policy> findRolePolicy(String userId) {
		return this.baseMapper.findRolePolicyList(userId);
	}

	public int policiesVote(String userId, String value) {
		List<Policy> policies = this.findUserPolicy(userId);
		if (CollUtil.isEmpty(policies)) {
			policies = CollUtil.newArrayList();
		}
		policies.addAll(this.findRolePolicy(userId));
		if (policies.isEmpty()) {
			return -1;
		}

		Set<Statement> statementSets = new HashSet<Statement>();
		policies.stream().forEach(e -> {
			List<Statement> statements = JSON.parseArray(e.getContent(), Statement.class);
			statementSets.addAll(statements);
		});
		Iterator<Statement> iters = statementSets.iterator();
		while (iters.hasNext()) {
			Statement statement = iters.next();
			Iterator<String> itersP = statement.getPermissions().iterator();
			while (itersP.hasNext()) {
				String policy = itersP.next();
				if (antPathMatcher.match(policy, value) && "-1".equals(statement.getEffect())) {
					return -1;
				}
			}
		}
		return 1;
	}

}
