/**
 *
 */
package com.tangdao.modules.sys.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.model.domain.Policy;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年7月7日
 */
@Mapper
public interface PolicyMapper extends BaseMapper<Policy>{

	public List<Policy> findUserPolicyList(String userId);
	
	public List<Policy> findRolePolicyList(String userId);
}
