/**
 *
 */
package com.tangdao.modules.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tangdao.model.domain.User;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月28日
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	
}
