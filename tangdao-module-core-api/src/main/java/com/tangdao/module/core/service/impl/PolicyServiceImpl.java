package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.entity.Policy;
import com.tangdao.module.core.mapper.PolicyMapper;
import com.tangdao.module.core.service.IPolicyService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 策略表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Service
public class PolicyServiceImpl extends CrudServiceImpl<PolicyMapper, Policy> implements IPolicyService {

}
