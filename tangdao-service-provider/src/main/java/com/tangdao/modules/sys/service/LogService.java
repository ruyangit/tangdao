/**
 *
 */
package com.tangdao.modules.sys.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tangdao.core.service.BaseService;
import com.tangdao.core.web.aspect.AuditLogService;
import com.tangdao.core.web.aspect.model.Log;
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

//	@Autowired
//	private DemoDataPrivilegeAnnotationHandler demoDataPrivilegeAnnotationHandler;
//	
//	@Autowired
//	private DemoDataPrivilegeFilter demoDataPrivilegeFilter;
//	
//	@Autowired
//	private DemoDataPrivilegeProvider demoDataPrivilegeProvider;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveAuditLog(Log log) {
		// TODO Auto-generated method stub
		this.baseMapper.insert(log);
	}

	public IPage<Log> findPage(Page<Log> page) {
//		Map<String, Boolean> filterData = new HashMap<String, Boolean>();
//		filterData.put("createByKey", Boolean.TRUE);
//		Map<String, Object> privilegeData = new HashMap<String, Object>();
//		privilegeData.put("createByKey", SessionContext.getUserId());
//
//		try {
//			Method method = LogMapper.class.getMethod("findPage", new Class[] { Page.class });
//			DataPrivilege dataPrivilege = method.getAnnotation(DataPrivilege.class);
//
//			demoDataPrivilegeFilter.setFilterData(filterData);
//			demoDataPrivilegeProvider.setPrivilegeData(privilegeData);
//			demoDataPrivilegeAnnotationHandler.setDataPrivilege(dataPrivilege);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return this.baseMapper.findPage(page);
	}

}
