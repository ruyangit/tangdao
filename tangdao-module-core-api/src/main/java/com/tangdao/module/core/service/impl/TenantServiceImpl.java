package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.mapper.TenantMapper;
import com.tangdao.module.core.model.domain.Tenant;
import com.tangdao.module.core.service.ITenantService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 租户表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-22
 */
@Service
public class TenantServiceImpl extends CrudServiceImpl<TenantMapper, Tenant> implements ITenantService {

}
