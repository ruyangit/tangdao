/**
 *
 */
package com.tangdao.core.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static Logger logger = LoggerFactory.getLogger(SessionContext.class);

	private static final ThreadLocal<SessionUser> LOCAL = ThreadLocal.withInitial(SessionUser::new);

	public static SessionUser get() {
		if (LOCAL.get() == null) {
			logger.error("session context not exist");
			return null;
		}
		return LOCAL.get();
	}

	public static String getId() {
		SessionUser session = get();
		if (session != null) {
			return session.getId();
		}
		return null;
	}

	public static void set(SessionUser session) {
		if (LOCAL.get() != null) {
			LOCAL.remove();
		}
		LOCAL.set(session);
	}

	public static void remove() {
		try {
			if (LOCAL.get() != null) {
				LOCAL.remove();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
