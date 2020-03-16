package com.tangdao.module.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tangdao.framework.model.UserInfo;
import com.tangdao.module.core.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

	/**
	 * 查询用户信息列表
	 * @param user {loginName:'必填',password:'可选'}
	 * @return
	 */
	public List<UserInfo> listUserInfo(UserInfo user);
}
