/**
 *
 */
package com.tangdao.portal.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.portal.model.domain.Role;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
public interface RoleMapper extends BaseMapper<Role> {

	public List<Role> findByUserRole(Role role);

	public int deleteRoleUser(@Param("roleCode") String roleCode);

	public int insertRoleUser(@Param("roleCode") String roleCode, @Param("userCodes") List<String> userCode);

	public int deleteRoleMenu(@Param("roleCode") String roleCode);

	public int insertRoleMenu(@Param("roleCode") String roleCode, @Param("menuCodes") List<String> menuCodes);

}
