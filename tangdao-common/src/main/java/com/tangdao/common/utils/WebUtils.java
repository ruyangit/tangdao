/**
 *
 */
package com.tangdao.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.tangdao.common.CommonResponse;
import com.tangdao.common.constant.ErrorCode;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;

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

	public static String decode(String str, String encode) throws UnsupportedEncodingException {
		return innerDecode(null, str, encode);
	}

	private static String innerDecode(String pre, String now, String encode) throws UnsupportedEncodingException {
		// Because the data may be encoded by the URL more than once,
		// it needs to be decoded recursively until it is fully successful
		if (StringUtils.equals(pre, now)) {
			return pre;
		}
		pre = now;
		now = URLDecoder.decode(now, encode);
		return innerDecode(pre, now, encode);
	}

	private static String resolveValue(String value, String encoding) {
		if (StringUtils.isBlank(encoding)) {
			encoding = StandardCharsets.UTF_8.name();
		}
		try {
			value = decode(new String(value.getBytes(StandardCharsets.UTF_8), encoding), encoding);
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

	public static void downFile(File file, HttpServletRequest request, HttpServletResponse response, String fileName) {
		if (file == null || !file.exists() || file.length() <= 0) {
			return;
		}
		try (RandomAccessFile randomFile = new RandomAccessFile(file, "r");
				ServletOutputStream out = response.getOutputStream()) {
			long contentLength = randomFile.length();
			String range = request.getHeader("Range");
			long start = 0, end = 0;
			if (range != null && range.startsWith("bytes=")) {
				String[] values = range.split("=")[1].split("-");
				start = Long.parseLong(values[0]);
				if (values.length > 1) {
					end = Long.parseLong(values[1]);
				}
			}
			int requestSize = 0;
			if (end != 0 && end > start) {
				requestSize = Long.valueOf(end - start + 1).intValue();
			} else {
				requestSize = Integer.MAX_VALUE;
			}
			String contentType = null;
			try {
				contentType = new MimetypesFileTypeMap().getContentType(file);
			} catch (Exception e) {
			}
			if (contentType != null) {
				response.setContentType(contentType);
			}

			// video/mp4 mp4
			// video/webm webm
			boolean isPreview = "preview".equalsIgnoreCase(request.getParameter("source"));
			response.addHeader("Content-Disposition", (!isPreview ? "attachment; " : "") + "filename*=utf-8'zh_cn'"
					+ URLUtil.encode(StringUtils.isBlank(fileName) ? file.getName() : fileName));
			response.setHeader("Accept-Ranges", "bytes");
			// 第一次请求只返回 content length 来让客户端请求多次实际数据
			if (range == null) {
				response.setHeader("Content-Length", String.valueOf(contentLength));
			} else {
				// 以后的多次以断点续传的方式来返回视频数据
				response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206
				long requestStart = 0, requestEnd = 0;
				String[] ranges = range.split("=");
				if (ranges.length > 1) {
					String[] rangeDatas = ranges[1].split("-");
					requestStart = Long.parseLong(rangeDatas[0]);
					if (rangeDatas.length > 1) {
						requestEnd = Long.parseLong(rangeDatas[1]);
					}
				}
				long length = 0;
				if (requestEnd > 0) {
					length = requestEnd - requestStart + 1;
					response.setHeader("Content-Length", String.valueOf(length));
					response.setHeader("Content-Range",
							"bytes " + requestStart + "-" + requestEnd + "/" + contentLength);
				} else {
					length = contentLength - requestStart;
					response.setHeader("Content-Length", String.valueOf(length));
					response.setHeader("Content-Range",
							"bytes " + requestStart + "-" + (contentLength - 1) + "/" + contentLength);
				}
			}
			randomFile.seek(start);
			int needSize = requestSize;
			while (needSize > 0) {
				byte[] buffer = new byte[1024];
				int len = randomFile.read(buffer);
				if (needSize < buffer.length) {
					out.write(buffer, 0, needSize);
				} else {
					out.write(buffer, 0, len);
					if (len < buffer.length) {
						break;
					}
				}
				needSize -= buffer.length;
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPath(String path) {
		String p = StringUtils.replace(path, "\\", "/");
		p = StringUtils.join(StringUtils.split(p, "/"), "/");
		if (!StringUtils.startsWithAny(p, "/") && StringUtils.startsWithAny(path, "\\", "/")) {
			p += "/";
		}
		if (!StringUtils.endsWithAny(p, "/") && StringUtils.endsWithAny(path, "\\", "/")) {
			p = p + "/";
		}
		if (path != null && path.startsWith("/")) {
			p = "/" + p; // linux下路径
		}
		return p;
	}

	public static String getReplacePathDkh(String path) {
		String[] fmts = StringUtils.substringsBetween(path, "{", "}");
		if (path != null) {
			for (String fmt : fmts) {
				if (StrUtil.isNotBlank(fmt) && StrUtil.containsAny(fmt, "yyyy", "MM", "dd", "HH", "mm", "ss", "E")) {
					path = StringUtils.replace(path, "{" + fmt + "}", DateUtil.format(new Date(), fmt));
				}
			}
		}
		return path;
	}

	public static String getUserfilesBaseDir(String baseDir, String path) {
		if (StringUtils.isBlank(baseDir)) {
			baseDir = getRequest().getSession().getServletContext().getRealPath("/");
		}
		if (StringUtils.isBlank(baseDir)) {
			baseDir = System.getProperty("user.dir");
		}
		if (!baseDir.endsWith("/")) {
			baseDir = baseDir + "/";
		}
		return StringUtils.isEmpty(path) ? getPath(path) : getPath(baseDir + "/userfiles/" + path);
	}

}
