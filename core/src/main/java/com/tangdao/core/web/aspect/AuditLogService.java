/**
 *
 */
package com.tangdao.core.web.aspect;

import com.tangdao.core.model.domain.Log;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月29日
 */
public interface AuditLogService {

	/**
	 * TODO 保存审计内容（日志）
	 * 
	 * @param log
	 */
	public void saveAuditLog(Log log);
}
