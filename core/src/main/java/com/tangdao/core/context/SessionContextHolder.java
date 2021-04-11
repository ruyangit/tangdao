/**
 *
 */
package com.tangdao.core.context;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月19日
 */
public class SessionContextHolder {

	private static final ThreadLocal<SessionUser> LOCAL = ThreadLocal.withInitial(SessionUser::new);

	private SessionContextHolder() {
	}

	/**
	 * 
	 * @return
	 */
	public static String getId() {
		SessionUser session = get();
		if (session != null) {
			return session.getId();
		}
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public static SessionUser get() {
		return LOCAL.get();
	}

	/**
	 * 
	 * @param session
	 */
	public static void set(SessionUser session) {
		LOCAL.set(session);
	}

	/**
	 * 
	 */
	public static void remove() {
		LOCAL.remove();
	}
}
