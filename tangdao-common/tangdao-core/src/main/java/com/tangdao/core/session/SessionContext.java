/**
 *
 */
package com.tangdao.core.session;

import java.io.Serializable;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年6月15日
 */
public class SessionContext {

	private static final ThreadLocal<TSession> LOCAL = ThreadLocal.withInitial(TSession::new);

	private SessionContext() {
	}

	/**
	 * TODO 获取session信息
	 *
	 * @return session
	 */
	public static TSession getSession() {
		return LOCAL.get();
	}

	/**
	 * TODO 获取当前登录用户的id
	 *
	 * @return id
	 */
	public static Serializable getId() {
		TSession session = getSession();
		if (session == null)
			return null;
		return session.getId();
	}

	/**
	 * TODO 设置session信息
	 *
	 * @param session session
	 */
	public static void setSession(TSession session) {
		LOCAL.set(session);
	}

	/**
	 * TODO 移除session信息
	 */
	public static void removeSession() {
		LOCAL.remove();
	}
}
