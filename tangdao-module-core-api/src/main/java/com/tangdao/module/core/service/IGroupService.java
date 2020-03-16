package com.tangdao.module.core.service;

import java.util.List;

import com.tangdao.framework.service.ICrudService;
import com.tangdao.module.core.model.domain.Group;

/**
 * <p>
 * 用户组表 服务类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-03-11
 */
public interface IGroupService extends ICrudService<Group> {

	/**
	 * @todo 用户组数据
	 * @param userId
	 * @return
	 */
	public List<Group> listGroupsForUser(String userId);
}
