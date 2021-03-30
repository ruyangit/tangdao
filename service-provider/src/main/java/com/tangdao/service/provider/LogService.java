/**
 *
 */
package com.tangdao.service.provider;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.SysLog;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.service.ILogService;
import com.tangdao.service.dao.LogMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月9日
 */
@Service
public class LogService extends BaseService<LogMapper, SysLog> implements ILogService {

}
