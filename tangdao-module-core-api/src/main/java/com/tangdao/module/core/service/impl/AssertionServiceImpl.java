package com.tangdao.module.core.service.impl;

import com.tangdao.module.core.mapper.AssertionMapper;
import com.tangdao.module.core.model.domain.Assertion;
import com.tangdao.module.core.service.IAssertionService;
import com.tangdao.framework.service.impl.CrudServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 断言表 服务实现类
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020-02-26
 */
@Service
public class AssertionServiceImpl extends CrudServiceImpl<AssertionMapper, Assertion> implements IAssertionService {

}
