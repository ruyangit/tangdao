/**
 * 
 */
package com.tangdao.core.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.tangdao.core.CommonResponse;
import com.tangdao.core.constant.ErrorCode;
/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2020年12月24日
 */
public class ServletUtil {

	/**
	 * 获取当前请求对象
	 * web.xml: <listener><listener-class>
	 * 	org.springframework.web.context.request.RequestContextListener
	 * 	</listener-class></listener>
	 */
	public static HttpServletRequest getRequest(){
		HttpServletRequest request = null;
		try{
			request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
			if (request == null){
				return null;
			}
			return request;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * 获取当前相应对象
	 * web.xml: <filter><filter-name>requestContextFilter</filter-name><filter-class>
	 * 	org.springframework.web.filter.RequestContextFilter</filter-class></filter><filter-mapping>
	 * 	<filter-name>requestContextFilter</filter-name><url-pattern>/*</url-pattern></filter-mapping>
	 */
	public static HttpServletResponse getResponse(){
		HttpServletResponse response = null;
		try{
			response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
			if (response == null){
				return null;
			}
		}catch(Exception e){
			return null;
		}
		return response;
	}
	
	/**
	 * 获得请求参数值
	 */
	public static String getParameter(String name) {
		HttpServletRequest request = getRequest();
		if (request == null){
			return null;
		}
		return request.getParameter(name);
	}
	
	/**
	 * 获得请求参数Map
	 */
	public static Map<String, Object> getParameters() {
		return getParameters(getRequest());
	}
	
	/**
	 * 获得请求参数Map
	 */
	public static Map<String, Object> getParameters(ServletRequest request) {
		if (request == null){
			return new HashMap<String, Object>();
		}
		return getParametersStartingWith(request, "");
	}
	
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 返回的结果的Parameter名已去除前缀.
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(pre) || paramName.startsWith(pre)) {
				String unprefixed = paramName.substring(pre.length());
				String[] values = request.getParameterValues(paramName);
				if (values == null || values.length == 0) {
					values = new String[]{};
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}

	/**
	 * 组合Parameters生成Query String的Parameter部分,并在paramter name上加上prefix.
	 */
	public static String encodeParameterStringWithPrefix(Map<String, Object> params, String prefix) {
		StringBuilder queryStringBuilder = new StringBuilder();
		String pre = prefix;
		if (pre == null) {
			pre = "";
		}
		Iterator<Entry<String, Object>> it = params.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			queryStringBuilder.append(pre).append(entry.getKey()).append("=").append(entry.getValue());
			if (it.hasNext()) {
				queryStringBuilder.append("&");
			}
		}
		return queryStringBuilder.toString();
	}
	
	/**
	 * 
	 * TODO
	 * @param response
	 * @param body
	 * @param code
	 * @throws IOException
	 */
	public static void response(HttpServletResponse response, String body, int code) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(body);
		response.setStatus(code);
	}
	
	/**
	 * 
	 * TODO
	 * @param response
	 * @param errorCode
	 * @throws IOException
	 */
	public static void responseJson(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(errorCode);
		response(response, JSON.toJSONString(commonResponse), 200);
	}

	/**
	 * 
	 * TODO
	 * @param response
	 * @param errorCode
	 * @param message_description
	 * @throws IOException
	 */
	public static void responseJson(HttpServletResponse response, ErrorCode errorCode, String message_description)
			throws IOException {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(errorCode);
		// 异常描述
		commonResponse.put("message_description", message_description);
		response(response, JSON.toJSONString(commonResponse), 200);
	}

}
