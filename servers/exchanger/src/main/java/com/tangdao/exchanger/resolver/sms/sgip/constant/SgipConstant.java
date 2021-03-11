package com.tangdao.exchanger.resolver.sms.sgip.constant;

import java.util.HashMap;
import java.util.Map;

public final class SgipConstant {

	/**
	 * 计费类型
	 */
	public static int FEE_TYPE = 1;

	/**
	 * 该条短消息的收费值
	 */
	public static String FEE_VALUE = "000";

	/**
	 * 代收费标志0：应收1：实收
	 */
	public static int FEE_CODE = 0;

	/**
	 * GSM协议类型
	 */
	public static int TP_PID = 0;
	public static int TP_UDHI = 0;

	public static final int MSG_FMT_GBK = 15;
	public static final int MSG_FMT_UCS2 = 8;

	/**
	 * GBK编码单条最大字节数
	 */
	public static final int MAX_MESSAGE_GBK_LENGTH = 140;
	/**
	 * UCS2编码单挑最大字节数
	 */
	public static final int MAX_MESSAGE_UCS2_LENGTH = 140;

	/**
	 * 网关常规状态回执成功码（可在通道模板中自定义）
	 */
	public static final String COMMON_MT_STATUS_SUCCESS_CODE = "DELIVRD";

	/**
	 * SGIP 重连计时(毫秒)
	 */
	public static volatile Map<String, Long> SGIP_RECONNECT_TIMEMILLS = new HashMap<>();

	/**
	 * HTTP本地监听IP节点名称
	 */
	public static final String NODE_NAME_LOCAL_IP = "local-ip";

	/**
	 ** HTTP本地监听IP节点数据
	 */
	public static final String DATA_LOCAL_IP = "127.0.0.1";

	/**
	 ** HTTP本地监听PORT节点名称
	 */
	public static final String NODE_NAME_LOCAL_PORT = "local-port";

	/**
	 ** HTTP本地监听PORT节点数据
	 */
	public static final int DATA_LOCAL_PORT = 8752;
}
