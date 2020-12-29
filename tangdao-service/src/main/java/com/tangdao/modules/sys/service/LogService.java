/**
 *
 */
package com.tangdao.modules.sys.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.model.Log;
import com.tangdao.core.mybatis.data.privilege.DataPrivilegeContext;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.web.aspect.AuditLogService;
import com.tangdao.modules.sys.mapper.LogMapper;

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

	public IPage<Log> findPage(Page<Log> page) {
		Map<String, Boolean> dataPrivilegeFilter = new HashMap<String, Boolean>();
		dataPrivilegeFilter.put("createByKey", Boolean.FALSE);
		DataPrivilegeContext.setDataPrivilegeFilter(dataPrivilegeFilter);
		return this.baseMapper.findPage(page);
	}

}
