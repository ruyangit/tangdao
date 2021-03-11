package com.tangdao.exchanger.resolver.sms.smgp.constant;

public final class SmgpConstant {

	// MT消息,对于回执消息该字段无效；
	// 对于文本短消息，该字段表示短消息的消息流向：0＝MO消息（终端发给SP）；
	// 6＝MT消息（SP发给终端，包括WEB上发送的点对点短消息）；
	// 7＝点对点短消息；
	public static int MSG_TYPE = 6;
	// SP是否要求返回状态报告 0＝不要求返回状态报告； 1＝要求返回状态报告；其它保留。
	public static int NEED_REPORT = 1;
	// 短消息发送优先级0＝低优先级；1＝普通优先级； 2＝较高优先级； 3＝高优先级
	public static int PRIORITY = 3;

	// 业务代码，用于固定网业务。通SPID
	public static String SERVICE_ID = "";

	// GSM协议类型
	public static int TP_PID = 0;
	// GSM协议类型
	public static int TP_UDHI = 0;

//	对计费用户采取的收费类型。
//	对于MO消息或点对点短消息，该字段无效。对于MT消息，该字段用法如下：
//	00＝免费，此时FixedFee和FeeCode无效；
//	01＝按条计信息费，此时FeeCode表示每条费用，FixedFee无效；
//	02＝按包月收取信息费，此时FeeCode无效，FixedFee表示包月费用；
//	03＝按封顶收取信息费，若按条收费的费用总和达到或超过封顶费后，则按照封顶费用收取信息费；若按条收费的费用总和没有达到封顶费用，则按照每条费用总和收取信息费。FeeCode表示每条费用，FixedFee表示封顶费用。
	public static String FEE_TYPE = "00";

	// 每条短消息费率，单位为“分”
	public static String FEE_CODE = "";

	// 短消息的包月费/封顶费，单位为“分”
	public static String FIXED_FEE = "";

	// // 存活有效期(单位:分钟)
	// public static Date valid_Time = null;
	// // 定时发送时间
	// public static Date at_Time = null;
	// 源终端MSISDN号码(为SP的服务代码或前缀, 为服务代码的长号码,
	// 网关将该号码完整的填到SMPP协议相应的destination_address字段，
	// 该号码最终在用户手机上显示为短消息的主叫号码) (没有可以为空)，短信接入号
//	public static String src_Terminal_Id = "";
	// 保留
	public static String RESERVER = "";

	public static final int MSG_FMT_GBK = 15;
	public static final int MSG_FMT_UCS2 = 8;

	public static final int LOGIN_MODE = 2;

	// 0x30
	public static final int DEFAULT_VERSION = 48;
	/**
	 * GBK编码单条最大字节数
	 */
	public static final int MAX_MESSAGE_GBK_LENGTH = 140;
	/**
	 * UCS2编码单挑最大字节数
	 */
	public static final int MAX_MESSAGE_UCS2_LENGTH = 140;

	// 公共状态回执成功码
	public static final String SMGP_MT_STATUS_SUCCESS_CODE = "000";

	// 公共状态回执成功码
	public static final String COMMON_MT_STATUS_SUCCESS_CODE = "DELIVRD";

}
