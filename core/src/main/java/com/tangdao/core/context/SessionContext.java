/**
 *
 */
package com.tangdao.core.context;

import com.tangdao.core.model.vo.SessionUser;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
public class SessionContext {

	/**
	 * 
	 */
	private static final ThreadLocal<SessionUser> LOCAL = ThreadLocal.withInitial(SessionUser::new);

	/**
	 * TODO 获取session信息
	 *
	 * @return session
	 */
	public static SessionUser getSession() {
		return LOCAL.get();
	}

	/**
	 * TODO 获取当前登录用户的id
	 *
	 * @return id
	 */
	public static String getId() {
		SessionUser session = getSession();
		if (session == null)
			return null; // 临时模拟数据
		return session.getId();
	}

	/**
	 * TODO 设置session信息
	 *
	 * @param session session
	 */
	public static void setSession(SessionUser session) {
		LOCAL.set(session);
	}

	/**
	 * TODO 移除session信息
	 */
	public static void removeSession() {
		LOCAL.remove();
	}
}
