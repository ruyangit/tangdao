/**
 *
 */
package com.tangdao.common.constant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年4月23日
 */
public class CommonConstants {
	
	/**
	 * yes = 1
	 */
	public static final String YES = "1";
	
	/**
	 * no = 0
	 */
	public static final String NO = "0";
	
	/**
	 * true
	 */
	public static final Boolean TRUE = true;
	
	/**
	 * false
	 */
	public static final Boolean FALSE = false;
	
	/**
	 * 服务间调用的认证token
	 */
	public static final String X_CLIENT_TOKEN = "x-client-token";
	
	/**
	 * 服务间调用token用户信息,格式为json { "username":"必须有" "自定义key:"value" }
	 */
	public static final String X_CLIENT_TOKEN_USER = "x-client-token-user";
	
	/**
     * TODO 日志链路追踪id信息头
     */
	public static final String TRACE_ID_HEADER = "x-traceId-header";
    /**
     * TODO 日志链路追踪id日志标志
     */
	public static final String LOG_TRACE_ID = "traceId";
	
	/**
	 * TODO 唯一请求 ID
	 */
	public static final String REQUEST_ID_HEADER = "x-requestId-header";
	
	/**
     * TODO 唯一请求 ID，每次请求都会返回。定位问题时需要提供该次请求的 RequestId。
     */
	public static final String REQUEST_ID = "requestId";
}
