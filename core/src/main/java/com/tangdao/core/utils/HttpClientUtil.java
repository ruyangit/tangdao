/**
 *
 */
package com.tangdao.core.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangdao.core.exception.DataEmptyException;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class HttpClientUtil {

	/**
	 * 从连接池中获取连接时间
	 */
	private static final int CONNECTION_REQUEST_TIMEOUT = 1000;
	/**
	 * 建立连接时间
	 */
	private static final int CONNECTION_TIMEOUT = 10 * 1000;
	/**
	 * 数据响应超时时间
	 */
	private static final int SOCKET_READ_TIMEOUT = 180 * 1000;

	/**
	 * 本次HTTP连接池
	 */
	private static volatile Map<String, PoolingHttpClientConnectionManager> LOCAL_HTTP_CLIENT_FACTORY = new HashMap<>();

	/**
	 * 默认最大连接池数量（针对整个域名主机）
	 */
	private static final Integer DEFAULT_MAX_TOTAL = 200;
	/**
	 * 每个路由最大连接数（真正限制）
	 */
	private static final Integer DEFAULT_MAX_PER_ROUTE = 100;

	private final static Object SYN_LOCK = new Object();
	/**
	 * 默认编码
	 */
	private static final String ENCODING_UTF_8 = "UTF-8";

	/**
	 * 成功码
	 */
	private static final int SUCCESS_RESPONSE_CODE = 200;

	/**
	 * HTTPS协议前缀名
	 */
	private static final String HTTPS_PROTOCOL_PREFIX = "https";

	/**
	 * URL HOST和PORT分隔符
	 */
	private static final String HOST_SEPARATOR = ":";

	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * 推送回执信息，如果用户回执success才算正常接收，否则重试，达到重试上限次数，抛弃
	 */
	private static final String PUSH_REPONSE_SUCCESS_CODE = "success";

	/**
	 * 推送重试上限次数
	 */
	private static final int PUSH_RETRY_TIMES = 3;

	/**
	 * 当前线程起始时间
	 */
	private static AtomicLong currentStartTime = new AtomicLong();

	/**
	 * URL是否为HTTPS
	 * 
	 * @param url 连接地址
	 * @return true/false
	 */
	private static boolean isHttpsUrl(String url) {
		return StrUtil.isNotBlank(url) && url.trim().startsWith(HTTPS_PROTOCOL_PREFIX);
	}

	private static CloseableHttpClient getHttpClient(String url, Integer maxTotal, Integer maxPerRoute) {
		String hostname = url.split("/")[2];

		// HTTP端口默认80，HTTPS默认443
		int port = isHttpsUrl(url) ? 433 : 80;
		if (hostname.contains(HOST_SEPARATOR)) {
			String[] arr = hostname.split(HOST_SEPARATOR);
			hostname = arr[0];
			port = Integer.parseInt(arr[1]);
		}

		String hostAndPort = String.format("%s_%d", hostname, port);

		// 判断当前连接池中是否存在HTTP 配置信息
		if (!LOCAL_HTTP_CLIENT_FACTORY.containsKey(hostAndPort)) {
			synchronized (SYN_LOCK) {
				LOCAL_HTTP_CLIENT_FACTORY.put(hostAndPort, bindHttpClientConnectionManager(maxTotal, maxPerRoute));
				logger.info("URL:{} 连接池初始化", url);
				logger.info("KEY：{} 连接池初始化成功，连接池总大小：{} 单路由池大小： {}", hostAndPort, maxTotal, maxPerRoute);
			}
		}

		return HttpClients.custom().setConnectionManager(LOCAL_HTTP_CLIENT_FACTORY.get(hostAndPort)).build();
	}

	/**
	 * 创建HttpClient对象
	 *
	 * @return
	 * @create 2015年12月18日
	 */
	private static PoolingHttpClientConnectionManager bindHttpClientConnectionManager(int maxTotal, int maxPerRoute) {
		ConnectionSocketFactory plainsf = PlainConnectionSocketFactory.getSocketFactory();
		LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory.getSocketFactory();
		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("http", plainsf).register("https", sslsf).build();

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
		// 将最大连接数增加
		cm.setMaxTotal(maxTotal);
		// 将每个路由基础的连接增加
		cm.setDefaultMaxPerRoute(maxPerRoute);

		// HttpHost httpHost = new HttpHost(hostname, port);

		// // 将目标主机的最大连接数增加
		// cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

		// 请求重试处理
		// HttpRequestRetryHandler httpRequestRetryHandler = new
		// HttpRequestRetryHandler() {
		// public boolean retryRequest(IOException exception,
		// int executionCount, HttpContext context) {
		// if (executionCount >= 2) {// 如果已经重试了2次，就放弃
		// return false;
		// }
		// if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
		// return true;
		// }
		// if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
		// return false;
		// }
		// if (exception instanceof InterruptedIOException) {// 超时
		// return false;
		// }
		// if (exception instanceof UnknownHostException) {// 目标服务器不可达
		// return false;
		// }
		// if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
		// return false;
		// }
		// if (exception instanceof SSLException) {// SSL握手异常
		// return false;
		// }
		//
		// HttpClientContext clientContext = HttpClientContext.adapt(context);
		// HttpRequest request = clientContext.getRequest();
		// // 如果请求是幂等的，就再次尝试
		// if (!(request instanceof HttpEntityEnclosingRequest)) {
		// return true;
		// }
		// return false;
		// }
		// };

		// CloseableHttpClient httpClient =
		// HttpClients.custom().setConnectionManager(cm).setRetryHandler(httpRequestRetryHandler).build();

		return cm;
	}

	/**
	 * GET请求URL获取内容
	 *
	 * @param url
	 * @return
	 * @create 2015年12月18日
	 */
	public static String get(String url) {
		HttpGet httpget = new HttpGet(url);
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
				.setConnectTimeout(CONNECTION_TIMEOUT).setSocketTimeout(SOCKET_READ_TIMEOUT).build();
		httpget.setConfig(requestConfig);

		CloseableHttpResponse response = null;
		try {
			response = getHttpClient(url, DEFAULT_MAX_TOTAL, DEFAULT_MAX_PER_ROUTE).execute(httpget,
					HttpClientContext.create());
			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity, "utf-8");
			EntityUtils.consume(entity);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) {

		String url = "https://app.cckeji.cc/code/huashi.php";

		Map<String, Object> params = new HashMap<>();
		params.put("msgId", "11111");

		try {
			post(url, params);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * POST请求（默认连接数）
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, Object> params) {
		return post(url, null, params);
	}

	/**
	 * POST请求（默认连接数）
	 *
	 * @param url
	 * @param params
	 * @param headers
	 * @return
	 */
	public static String post(String url, Map<String, Object> headers, Map<String, Object> params) {
		return post(url, headers, params, DEFAULT_MAX_TOTAL, DEFAULT_MAX_PER_ROUTE);
	}

	/**
	 * POST请求（默认连接数）
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, Object> params, Integer maxPerRoute) {
		return post(url, null, params, maxPerRoute);
	}

	/**
	 * POST请求（默认连接数）
	 *
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, Object> headers, Map<String, Object> params,
			Integer maxPerRoute) {
		return post(url, headers, params, DEFAULT_MAX_TOTAL, maxPerRoute);
	}

	/**
	 * 设置HTTP参数信息
	 *
	 * @param httpPost
	 * @param params
	 */
	private static void setHttpParameters(HttpPost httpPost, Map<String, Object> params) {
		setHttpParameters(httpPost, params, ENCODING_UTF_8);
	}

	/**
	 * 设置HTTP参数信息（包含编码）
	 *
	 * @param httpPost
	 * @param params
	 * @param encoding
	 */
	private static void setHttpParameters(HttpPost httpPost, Map<String, Object> params, String encoding) {
		if (CollUtil.isEmpty(params)) {
			return;
		}

		List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
		for (String key : params.keySet()) {
			pairs.add(new BasicNameValuePair(key, params.get(key).toString()));
		}

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs, encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.error("设置HTTP请求参数编码异常", e);
		}
	}

	/**
	 * 设置HTTP头信息
	 *
	 * @param httpPost
	 * @param headers
	 */
	private static void setHttpHeaders(HttpPost httpPost, Map<String, Object> headers) {
		setHttpHeaders(httpPost, headers, ENCODING_UTF_8);
	}

	/**
	 * 获取HttpPost
	 *
	 * @param url
	 * @return
	 */
	public static HttpPost getHttpPost(String url) {
		return getHttpPost(url, CONNECTION_REQUEST_TIMEOUT, CONNECTION_TIMEOUT, SOCKET_READ_TIMEOUT);
	}

	/**
	 * 获取HttpPost
	 *
	 * @param url
	 * @param connectionTimeout
	 * @return
	 */
	public static HttpPost getHttpPost(String url, int connectionTimeout) {
		return getHttpPost(url, CONNECTION_REQUEST_TIMEOUT, connectionTimeout, SOCKET_READ_TIMEOUT);
	}

	/**
	 * 获取HttpPost
	 *
	 * @param url
	 * @param conectionRequestTimeout 连接池获取连接超时时间
	 * @param connectionTimeout       连接URL超时时间
	 * @param socketTimeout           发送数据响应超时超时时间
	 * @return
	 */
	public static HttpPost getHttpPost(String url, int conectionRequestTimeout, int connectionTimeout,
			int socketTimeout) {
		HttpPost httpPost = new HttpPost(url);

		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(conectionRequestTimeout)
				.setConnectTimeout(connectionTimeout).setSocketTimeout(socketTimeout).build();
		httpPost.setConfig(requestConfig);

		return httpPost;
	}

	/**
	 * 设置HTTP头信息（包含编码）
	 *
	 * @param httpPost
	 * @param headers
	 * @param encoding
	 */
	private static void setHttpHeaders(HttpPost httpPost, Map<String, Object> headers, String encoding) {
		if (CollUtil.isEmpty(headers)) {
			return;
		}

		for (String key : headers.keySet()) {
			httpPost.addHeader(new BasicHeader(key, headers.get(key).toString()));
		}
	}

	/**
	 * 发送
	 *
	 * @param url
	 * @param headers
	 * @param params
	 * @param maxTotal
	 * @param maxPerRoute
	 * @return
	 */
	public static String post(String url, Map<String, Object> headers, Map<String, Object> params, Integer maxTotal,
			Integer maxPerRoute) {
		if (CollUtil.isEmpty(params)) {
			throw new DataEmptyException("用户参数为空");
		}

		long startTime = System.currentTimeMillis();
		HttpPost httpPost = getHttpPost(url);

		CloseableHttpResponse response;
		CloseableHttpClient httpClient = null;
		try {

			// 设置头信息
			setHttpHeaders(httpPost, headers);
			// 设置参数信息
			setHttpParameters(httpPost, params);

			httpClient = HttpClientUtil.getHttpClient(url, maxTotal, maxPerRoute);
			// 提交post请求
			response = httpClient.execute(httpPost);
			// 获取响应内容
			StatusLine statusLine = response.getStatusLine();
			// 响应码
			int statusCode = statusLine.getStatusCode();
			// String reasonPhrase = statusLine.getReasonPhrase();// 响应信息

			if (statusCode != SUCCESS_RESPONSE_CODE) {
				httpPost.abort();
				throw new RuntimeException(String.format("返回状态码失败，状态码为：%d", statusCode));
			}

			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;

		} catch (Exception e) {
			httpPost.abort();
			logger.error("URL{} 请求处理失败", url, e);
			throw new RuntimeException(String.format("URL: %s 调用失败！", url));
		} finally {
			logger.info("URL：{} 请求耗时：{} ms", url, System.currentTimeMillis() - startTime);
			// 释放资源
			httpPost.releaseConnection();
			// if (url.startsWith(HTTPS_PROTOCOL_PREFIX) && httpClient != null) {
			// try {
			// httpClient.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}

	}

	/**
	 * 调用连接池大小
	 *
	 * @param url
	 * @param params
	 * @param encoding
	 * @return
	 */
	public static String post(String url, Map<String, Object> params, String encoding) {
		return post(url, null, params, encoding, DEFAULT_MAX_TOTAL, DEFAULT_MAX_PER_ROUTE);
	}

	/**
	 * 调用连接池大小
	 *
	 * @param url
	 * @param headers
	 * @param params
	 * @param encoding
	 * @return
	 */
	public static String post(String url, Map<String, Object> headers, Map<String, Object> params, String encoding) {
		return post(url, headers, params, encoding, DEFAULT_MAX_TOTAL, DEFAULT_MAX_PER_ROUTE);
	}

	/**
	 * 调用POST
	 *
	 * @param url
	 * @param params
	 * @param encoding 编码方式
	 * @return
	 */
	public static String post(String url, Map<String, Object> headers, Map<String, Object> params, String encoding,
			Integer maxTotal, Integer maxPerRoute) {
		if (CollUtil.isEmpty(params)) {
			throw new DataEmptyException("用户参数为空");
		}

		long startTime = System.currentTimeMillis();
		HttpPost httpPost = getHttpPost(url);

		CloseableHttpResponse response;
		CloseableHttpClient httpClient = null;
		try {

			// 设置头信息
			setHttpHeaders(httpPost, headers, encoding);
			// 设置参数信息
			setHttpParameters(httpPost, params, encoding);

			httpClient = HttpClientUtil.getHttpClient(url, maxTotal, maxPerRoute);
			// 提交post请求
			response = httpClient.execute(httpPost);
			// 获取响应内容
			StatusLine statusLine = response.getStatusLine();
			// 响应码
			int statusCode = statusLine.getStatusCode();
			// String reasonPhrase = statusLine.getReasonPhrase();// 响应信息
			if (statusCode != SUCCESS_RESPONSE_CODE) {
				httpPost.abort();
				throw new RuntimeException(String.format("返回状态码失败，状态码为：%d", statusCode));
			}

			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, encoding);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;

		} catch (Exception e) {
			httpPost.abort();
			logger.error("URL{} 请求处理失败", url, e);
			throw new RuntimeException(String.format("URL: %s 调用失败！", url));
		} finally {
			logger.info("URL：{} 请求耗时：{} ms", url, System.currentTimeMillis() - startTime);
			// 释放资源
			httpPost.releaseConnection();
			// if (url.startsWith(HTTPS_PROTOCOL_PREFIX) && httpClient != null) {
			// try {
			// httpClient.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	/**
	 * 发送报文信息
	 *
	 * @param url
	 * @param content
	 * @return
	 */
	public static String postReport(String url, String content) {
		return postReport(url, content, ENCODING_UTF_8, null);
	}

	/**
	 * 发送报文信息
	 *
	 * @param url
	 * @param content
	 * @param headers
	 * @return
	 */
	public static String postReport(String url, String content, Map<String, Object> headers) {
		return postReport(url, content, ENCODING_UTF_8, headers);
	}

	/**
	 * HTTP 发送
	 *
	 * @param url     网络URL
	 * @param content
	 * @param headers
	 * @return
	 */
	public static String postReport(String url, String content, String encoding, Map<String, Object> headers) {
		if (StrUtil.isEmpty(content)) {
			throw new DataEmptyException("用户参数为空");
		}

		long startTime = System.currentTimeMillis();
		HttpPost httpPost = getHttpPost(url);

		CloseableHttpResponse response;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = HttpClientUtil.getHttpClient(url, DEFAULT_MAX_TOTAL, DEFAULT_MAX_PER_ROUTE);

			// 设置头信息
			setHttpHeaders(httpPost, headers);

			BasicHttpEntity httpEntity = new BasicHttpEntity();
			httpEntity.setContent(new ByteArrayInputStream(content.getBytes(encoding)));
			httpEntity.setContentEncoding(encoding);
			httpEntity.setContentType("application/json");
			httpPost.setEntity(httpEntity);

			// 提交post请求
			response = httpClient.execute(httpPost);
			// 获取响应内容
			StatusLine statusLine = response.getStatusLine();
			// 响应码
			int statusCode = statusLine.getStatusCode();
			if (statusCode != SUCCESS_RESPONSE_CODE) {
				httpPost.abort();
				throw new RuntimeException(String.format("返回状态码失败，状态码为：%d", statusCode));
			}

			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, encoding);
			}
			EntityUtils.consume(entity);
			response.close();
			return result;

		} catch (Exception e) {
			httpPost.abort();
			logger.error("URL{} 请求处理失败", url, e);
			throw new RuntimeException(String.format("URL: %s 求处理失败！", url));
		} finally {
			logger.info("URL：{} 请求耗时：{} ms", url, System.currentTimeMillis() - startTime);
			// 释放资源
			httpPost.releaseConnection();
			// if (url.startsWith(HTTPS_PROTOCOL_PREFIX) && httpClient != null) {
			// try {
			// httpClient.close();
			// } catch (IOException e) {
			// e.printStackTrace();
			// }
			// }
		}
	}

	public static RetryResponse postBody(String url, String body, int currentCount) {
		return postBody(url, body, PUSH_RETRY_TIMES, currentCount);
	}

	/**
	 * 调用用户回调地址（递归重试，目前专用推送客户数据报告 上行/下行）
	 * 
	 * @param url        推送回调地址（HTTP）
	 * @param body       推送报文内容
	 * @param retryTimes 重试次数（默认3次）
	 * @return
	 */
	public static RetryResponse postBody(String url, String body, int retryTimes, int currentCount) {
		RetryResponse retryResponse = new RetryResponse();
		currentStartTime.set(System.currentTimeMillis());
		if (StrUtil.isEmpty(url) || StrUtil.isEmpty(body)) {
			retryResponse.setResult("URL或内容为空");
			retryResponse.setTimeCost(System.currentTimeMillis() - currentStartTime.get());
			return retryResponse;
		}

		try {
			String result = postReport(url, body);
			retryResponse.setResult(StrUtil.isEmpty(result) ? PUSH_REPONSE_SUCCESS_CODE : result);
			retryResponse.setSuccess(true);

		} catch (Exception e) {
			logger.error("调用用户推送地址解析失败：{}， 错误信息：{}", url, e.getMessage());
			retryResponse.setResult("调用异常失败，" + e.getMessage());
		}

		if (!retryResponse.isSuccess() && currentCount <= retryTimes) {
			currentCount = currentCount + 1;
			retryResponse = postBody(url, body, retryTimes, currentCount);
		}

		retryResponse.setTimeCost(System.currentTimeMillis() - currentStartTime.get());
		retryResponse.setAttemptTimes(currentCount > retryTimes ? retryTimes : currentCount);
		return retryResponse;
	}

	/**
	 * 重试回执信息
	 * 
	 * @author zhengying
	 * @version V1.0
	 * @date 2018年4月24日 下午11:19:15
	 */
	public static class RetryResponse {

		/**
		 * 尝试次数
		 */
		private int attemptTimes = 0;

		/**
		 * 返回结果
		 */
		private String result;

		private boolean isSuccess = false;

		private long timeCost;

		public int getAttemptTimes() {
			return attemptTimes;
		}

		public void setAttemptTimes(int attemptTimes) {
			this.attemptTimes = attemptTimes;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public boolean isSuccess() {
			return isSuccess;
		}

		public void setSuccess(boolean isSuccess) {
			this.isSuccess = isSuccess;
		}

		public long getTimeCost() {
			return timeCost;
		}

		public void setTimeCost(long timeCost) {
			this.timeCost = timeCost;
		}

	}
}
