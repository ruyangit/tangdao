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

	public static String required(final HttpServletRequest req, final String key) {
		String value = req.getParameter(key);
		if (StringUtils.isBlank(value)) {
			throw new IllegalArgumentException("Param '" + key + "' is required.");
		}
		String encoding = req.getParameter("encoding");
		return resolveValue(value, encoding);
	}

	public static String optional(final HttpServletRequest req, final String key, final String defaultValue) {
		if (!req.getParameterMap().containsKey(key) || req.getParameterMap().get(key)[0] == null) {
			return defaultValue;
		}
		String value = req.getParameter(key);
		if (StringUtils.isBlank(value)) {
			return defaultValue;
		}
		String encoding = req.getParameter("encoding");
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

	public static String getAcceptEncoding(HttpServletRequest req) {
		String encode = StringUtils.defaultIfEmpty(req.getHeader("Accept-Charset"), StandardCharsets.UTF_8.name());
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

	public static void response(HttpServletResponse response, String body, int code) throws IOException {
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(body);
		response.setStatus(code);
	}
}
