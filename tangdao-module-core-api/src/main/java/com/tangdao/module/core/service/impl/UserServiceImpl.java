package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.entity.User;
import com.tangdao.module.core.mapper.UserMapper;
import com.tangdao.module.core.service.IUserService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, User> implements IUserService {

}
