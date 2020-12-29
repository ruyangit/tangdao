/**
 *
 */
package com.tangdao.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tangdao.core.service.BaseService;
import com.tangdao.core.web.aspect.AuditLogService;
import com.tangdao.model.domain.Log;
import com.tangdao.modules.sys.dao.LogMapper;

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
	@Transactional(rollbackFor = Exception.class)
	public void saveAuditLog(Log log) {
		// TODO Auto-generated method stub
		this.baseMapper.insert(log);
	}
}
