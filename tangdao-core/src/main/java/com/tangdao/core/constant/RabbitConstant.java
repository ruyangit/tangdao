/**
 *
 */
package com.tangdao.core.constant;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
public class RabbitConstant {

	/**
	 * 短信下行 已完成前置校验（包括用户授权、余额校验等），待处理归正逻辑队列
	 */
	public static final String MQ_SMS_MT_WAIT_PROCESS = "mq_sms_mt_wait_process";

	/**
	 * 短信点对点短信下行 已完成前置校验（包括用户授权、余额校验等），待处理归正逻辑队列
	 */
	public static final String MQ_SMS_MT_P2P_WAIT_PROCESS = "mq_sms_mt_p2p_wait_process";

}
