/**
 *
 */
package com.tangdao.core.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tangdao.core.utils.LogUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月22日
 */
public class LogInterceptor implements HandlerInterceptor {

	private static final ThreadLocal<Long> startTimeThreadLocal = new NamedThreadLocal<Long>("LogInterceptor");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		long beginTime = System.currentTimeMillis();// 1、开始时间
		startTimeThreadLocal.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		long beginTime = startTimeThreadLocal.get();// 得到线程绑定的局部变量（开始时间）
		long endTime = System.currentTimeMillis(); // 2、结束时间
		long executeTime = endTime - beginTime; // 3、获取执行时间
		startTimeThreadLocal.remove(); // 用完之后销毁线程变量数据
		// 保存日志
		try {
			LogUtil.saveLog(request, handler, null, null, ex, executeTime);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
