package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.entity.Group;
import com.tangdao.module.core.mapper.GroupMapper;
import com.tangdao.module.core.service.IGroupService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户组 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class GroupServiceImpl extends CrudServiceImpl<GroupMapper, Group> implements IGroupService {

}
