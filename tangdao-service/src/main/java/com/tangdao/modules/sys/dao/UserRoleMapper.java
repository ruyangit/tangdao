/**
 *
 */
package com.tangdao.modules.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.model.domain.UserRole;
import com.tangdao.model.dto.UserRoleDTO;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole>{
	
}
