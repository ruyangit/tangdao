/**
 *
 */
package com.tangdao.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.core.model.domain.User;
import com.tangdao.core.model.vo.UserRole;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public interface UserMapper extends BaseMapper<User> {

	public int deleteUserRole(UserRole userRole);

	public int insertUserRole(UserRole userRole);
}
