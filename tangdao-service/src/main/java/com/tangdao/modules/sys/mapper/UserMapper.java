/**
 *
 */
package com.tangdao.modules.sys.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tangdao.core.mybatis.pagination.Pageinfo;
import com.tangdao.model.domain.User;
import com.tangdao.model.dto.UserDTO;

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

	public IPage<Map<String, Object>> findMapsPage(Pageinfo page, @Param("user") UserDTO user);
	
}
