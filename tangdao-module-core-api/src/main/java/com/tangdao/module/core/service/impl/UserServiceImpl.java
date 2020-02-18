package com.tangdao.module.core.service.impl;

import org.springframework.stereotype.Service;

import com.tangdao.framework.service.impl.CrudServiceImpl;
import com.tangdao.module.core.mapper.UserMapper;
import com.tangdao.module.core.model.domain.User;
import com.tangdao.module.core.service.IUserService;

@Service
public class UserServiceImpl extends CrudServiceImpl<UserMapper, User> implements IUserService {

}
