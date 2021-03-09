/**
 *
 */
package com.tangdao.core.constant;

/**
 * <p>
 * TODO 通行证鉴权常量类
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class PassportConstant {

	/**
	 * 开发者接入唯一标识节点定义
	 */
	public static final String DEVELOPER_APP_KEY_NODE_NAME = "appkey";

	/**
	 * 开发者签名密钥节点定义
	 */
	public static final String DEVELOPER_APP_SECRET_NODE_NAME = "appsecret";

	/**
	 * 签名时间戳标识节点定义（传递为当前毫秒值）
	 */
	public static final String SIGNATURE_TIMESTAMP_NODE_NAME = "timestamp";

	/**
	 * 默认时间戳过期时间耗数（30秒）
	 */
	public static final int DEFAULT_EXPIRE_TIMESTAMP_MILLISECOND = 30 * 1000;

	/**
	 * 请求最大手机号码数量
	 */
	public static final int MAX_REQUEST_MOBILE_SIZE = 1000;

	/**
	 * 
	 * <p>
	 * TODO 短信节点常量定义
	 * </p>
	 *
	 * @author ruyang
	 * @since 2021年2月22日
	 */
	static class SmsNode {

		/**
		 * 短信手机号码节点定义
		 */
		public static final String SMS_MOBILE_NODE_NAME = "mobile";

		/**
		 * 短信内容节点定义
		 */
		public static final String SMS_CONTENT_NODE_NAME = "content";

	}

	/**
	 * 开发基础节点信息
	 */
	public static final String[] DEVELOPER_SIGNATURE_NODE_NAMES = { DEVELOPER_APP_KEY_NODE_NAME,
			DEVELOPER_APP_SECRET_NODE_NAME, SIGNATURE_TIMESTAMP_NODE_NAME };

	/**
	 * 短信节点信息
	 */
	public static final String[] SMS_NODE_NAMES = { DEVELOPER_APP_KEY_NODE_NAME, DEVELOPER_APP_SECRET_NODE_NAME,
			SIGNATURE_TIMESTAMP_NODE_NAME, SmsNode.SMS_MOBILE_NODE_NAME, SmsNode.SMS_CONTENT_NODE_NAME };

	/**
	 * 异常通道ID
	 */
	public static final String EXCEPTION_PASSAGE_ID = "0";

	/**
	 * 异常（根据ID找不到通道等情况）显示的通道名称
	 */
	public static final String EXCEPTION_PASSAGE_NAME = "N/A";
}
