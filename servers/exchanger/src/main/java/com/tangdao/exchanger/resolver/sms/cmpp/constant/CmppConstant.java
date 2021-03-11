package com.tangdao.exchanger.resolver.sms.cmpp.constant;

/**
 * TODO CMPP协议常量类
 * 
 * @author ruyang
 * @version V1.0
 * @date 2017年7月2日 上午11:02:42
 */
public final class CmppConstant {

	/**
	 * Pk_total 相同Msg_Id的信息总条数
	 */
	public static int PK_TOTAL = 1;

	/**
	 * Pk_number 相同Msg_Id的信息序号，从1开始
	 */
	public static int PK_NUMBER = 1;

	/**
	 * 是否需要状态报告，0:不需要，1:需要，2:产生SMS话单
	 */
	public static int REGISTERED_DELIVERY = 1;

	/**
	 * 信息级别
	 */
	public static int MSG_LEVEL = 1;

	/**
	 * 计费用户类型字段
	 * 0：对目的终端MSISDN计费；1：对源终端MSISDN计费；2：对SP计费;3：表示本字段无效，对谁计费参见Fee_terminal_Id字段。
	 */
	public static int FEE_USERTYPE = 2;

	/**
	 * TP_pId，GSM协议类型
	 */
	public static int TP_PID = 0;

	/**
	 * GSM协议类型。详细是解释请参考 GSM03.40中的9.2.3.23,仅使用1位，右对齐。
	 */
	public static int TP_UDHI = 0;

	/**
	 * 资费类别：01：对“计费用户号码”免费；02：对“计费用户号码”按条计信息费；03：对“计费用户号码”按包月收取信息费。
	 */
	public static String FEE_TYPE = "02";

	/**
	 * 资费代码（以分为单位）。
	 */
	public static String FEE_CODE = "000";

	/**
	 * 存活有效期(单位:分钟)
	 */
	// public static Date valid_Time = null;

	/**
	 * 定时发送时间
	 */
	// public static Date at_Time = null;
	// 源终端MSISDN号码(为SP的服务代码或前缀, 为服务代码的长号码,
	// 网关将该号码完整的填到SMPP协议相应的destination_address字段，
	// 该号码最终在用户手机上显示为短消息的主叫号码) (没有可以为空)，短信接入号
	// public static String src_Terminal_Id = "";

	/**
	 * 保留
	 */
	public static String RESERVER = "";

	/**
	 * 信息格式：0：ASCII串；3：短信写卡操作；4：二进制信息；8：UCS2编码；15：含GB汉字。。。。。。
	 */
	public static final int MSG_FMT_GBK = 15;
	public static final int MSG_FMT_UCS2 = 8;

	/**
	 * 默认版本号
	 */
	public static final int DEFAULT_VERSION = 32;

	/**
	 * GBK编码单条最大字节数
	 */
	public static final int MAX_MESSAGE_GBK_LENGTH = 140;
	/**
	 * UCS2编码单挑最大字节数
	 */
	public static final int MAX_MESSAGE_UCS2_LENGTH = 140;

	/**
	 * 公共状态回执成功码
	 */
	public static final String COMMON_MT_STATUS_SUCCESS_CODE = "DELIVRD";

	/** -----------------------------以下为CMPP3特有属性----------------------------- */
	/** 详细参考url: https://www.cnblogs.com/tuyile006/p/5707271.html */

	/**
	 * 被计费用户的号码类型，0：真实号码；1：伪码
	 */
	public static final int FEE_TERMINAL_TYPE = 0;

	/**
	 * 接收短信的用户的号码类型，0：真实号码；1：伪码
	 */
	public static final int DEST_TERMINAL_TYPE = 0;

	/**
	 * 点播业务使用的LinkID，非点播类业务的MT流程不使用该字段。
	 */
	public static final String LINK_ID = "";
}
