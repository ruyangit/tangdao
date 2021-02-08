/**
 *
 */
package com.tangdao.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.system.entity.Role;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
public interface RoleMapper extends BaseMapper<Role> {

	public List<Role> findByUserCode(Role role);

	public int deleteRoleUser(@Param("roleCode") String roleCode, @Param("userCode") String userCode);

	public int insertRoleUser(@Param("roleCode") String roleCode, @Param("userCode") String userCode);

	public int deleteRoleMenu(@Param("roleCode") String roleCode);

	public int insertRoleMenu(@Param("roleCode") String roleCode, @Param("menuCodes") String[] menuCodes);

}
