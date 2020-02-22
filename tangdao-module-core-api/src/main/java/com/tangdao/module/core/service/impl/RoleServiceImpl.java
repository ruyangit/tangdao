package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.entity.Role;
import com.tangdao.module.core.mapper.RoleMapper;
import com.tangdao.module.core.service.IRoleService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class RoleServiceImpl extends CrudServiceImpl<RoleMapper, Role> implements IRoleService {

}
