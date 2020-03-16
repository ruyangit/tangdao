package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.mapper.GroupMapper;
import com.tangdao.module.core.model.domain.Group;
import com.tangdao.module.core.service.IGroupService;
import com.tangdao.framework.service.impl.CrudServiceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户组表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-03-11
 */
@Service
public class GroupServiceImpl extends CrudServiceImpl<GroupMapper, Group> implements IGroupService {

	@Override
	public List<Group> listGroupsForUser(String userId) {
		// TODO Auto-generated method stub
		return this.getBaseMapper().listGroupsForUser(userId);
	}

}
