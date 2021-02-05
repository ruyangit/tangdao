/**
 *
 */
package com.tangdao.system.service;

import org.springframework.stereotype.Service;

import com.tangdao.core.model.Log;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.web.aspect.AuditLogService;
import com.tangdao.system.mapper.LogMapper;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月5日
 */
@Service
public class LogService extends BaseService<LogMapper, Log> implements AuditLogService {

	@Override
	public void saveAuditLog(Log log) {
		this.baseMapper.insert(log);
	}
}
