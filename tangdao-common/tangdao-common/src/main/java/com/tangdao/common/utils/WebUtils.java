/**
 *
 */
package com.tangdao.common.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.ErrorCode;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang@gmail.com
 * @since 2020年5月29日
 */
public class WebUtils {

	public static String USER_AGENT_HEADER = "User-Agent";
	public static String CLIENT_VERSION_HEADER = "Client-Version";
	public static String REQUEST_SOURCE_HEADER = "Request-Source";

	public static HttpServletRequest getRequest() {
		HttpServletRequest request = null;
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			if (request == null) {
				return null;
			}
			return request;
		} catch (Exception e) {
			return null;
		}
	}

	public static HttpServletResponse getResponse() {
		HttpServletResponse response = null;
		try {
			response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
			if (response == null) {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
		return response;
	}

	public static String required(final HttpServletRequest request, final String key) {
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Param '" + key + "' is required.");
		}
		String encoding = request.getParameter("encoding");
		return resolveValue(value, encoding);
	}

	public static String optional(final HttpServletRequest request, final String key, final String defaultValue) {
		if (!request.getParameterMap().containsKey(key) || request.getParameterMap().get(key)[0] == null) {
			return defaultValue;
		}
		String value = request.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		String encoding = request.getParameter("encoding");
		return resolveValue(value, encoding);
	}

	private static String resolveValue(String value, String encoding) {
		if (StringUtils.isBlank(encoding)) {
			encoding = StandardCharsets.UTF_8.name();
		}
		try {
			value = HttpUtils.decode(new String(value.getBytes(StandardCharsets.UTF_8), encoding), encoding);
		} catch (UnsupportedEncodingException ignore) {
		}
		return value.trim();
	}

	public static String getAcceptEncoding(HttpServletRequest request) {
		String encode = StringUtils.defaultIfEmpty(request.getHeader("Accept-Charset"), StandardCharsets.UTF_8.name());
		encode = encode.contains(",") ? encode.substring(0, encode.indexOf(",")) : encode;
		return encode.contains(";") ? encode.substring(0, encode.indexOf(";")) : encode;
	}

	/**
	 * Returns the value of the request header "user-agent" as a
	 * <code>String</code>.
	 *
	 * @param request HttpServletRequest
	 * @return the value of the request header "user-agent", or the value of the
	 *         request header "client-version" if the request does not have a header
	 *         of "user-agent"
	 */
	public static String getUserAgent(HttpServletRequest request) {
		String userAgent = request.getHeader(USER_AGENT_HEADER);
		if (StringUtils.isBlank(userAgent)) {
			userAgent = StringUtils.defaultIfEmpty(request.getHeader(CLIENT_VERSION_HEADER), StringUtils.EMPTY);
		}
		return userAgent;
	}

	public static String getClientIP() {
		return getClientIP(getRequest());
	}

	public static String getClientIP(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}

		return getClientIPByHeader(request, headers);
	}

	public static String getClientIPByHeader(HttpServletRequest request, String... headerNames) {
		String ip;
		for (String header : headerNames) {
			ip = request.getHeader(header);
			if (!NetUtil.isUnknow(ip)) {
				return NetUtil.getMultistageReverseProxyIp(ip);
			}
		}

		ip = request.getRemoteAddr();
		return NetUtil.getMultistageReverseProxyIp(ip);
	}

	public static void response(HttpServletResponse response, String body, int code) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(body);
		response.setStatus(code);
	}

	public static void responseJson(HttpServletResponse response, String body) throws IOException {
		response(response, body, 200);
	}

	public static void responseJson(HttpServletResponse response, Object body) throws IOException {
		response(response, JSON.toJSONString(body), 200);
	}

	public static void responseJson(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(errorCode);
		responseJson(response, JSON.toJSONString(commonResponse));
	}

	public static void responseJson(HttpServletResponse response, ErrorCode errorCode, String error)
			throws IOException {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		commonResponse.fail(errorCode);
		// 异常描述
		commonResponse.put("message_description", error);
		responseJson(response, JSON.toJSONString(commonResponse));
	}
}
