package com.tangdao.module.core.service.impl;

import org.springframework.stereotype.Service;

import com.tangdao.framework.service.impl.TreeServiceImpl;
import com.tangdao.module.core.mapper.MenuMapper;
import com.tangdao.module.core.model.domain.Menu;
import com.tangdao.module.core.service.IMenuService;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class MenuServiceImpl extends TreeServiceImpl<MenuMapper, Menu> implements IMenuService {

}
