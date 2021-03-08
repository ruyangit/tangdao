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
 * @since 2021年3月3日
 */
public class SmsRedisConstant {

	/**
	 * 待持久数据库任务集合（高级别）
	 */
	public static final String RED_TASK_PERSISTENCE_HIGH = "red_task_persistence_high";

	/**
	 * 待持久数据库任务集合（低级别）
	 */
	public static final String RED_TASK_PERSISTENCE_LOW = "red_task_persistence_low";

	/**
	 * 短信通道
	 */
	public static final String RED_SMS_PASSAGE = "red_sms_passage";

	/**
	 * 短信敏感词缓存信息
	 */
	public static final String RED_FORBIDDEN_WORDS = "red_forbidden_words";

	/**
	 * 手机号码黑名单数据
	 */
	public static final String RED_MOBILE_BLACKLIST = "red_mobile_blacklist";

	/**
	 * 手机号码白名单数据
	 */
	public static final String RED_MOBILE_WHITELIST = "red_mobile_whitelist";

	/**
	 * 用户可用通道前缀
	 */
	public static final String RED_USER_PASSAGE_ACCESS = "red_user_passage_access";

	/**
	 * 用户短信模板
	 */
	public static final String RED_USER_MESSAGE_TEMPLATE = "red_user_message_template";

	/**
	 * 用户签名扩展号码
	 */
	public static final String RED_USER_SIGNATURE_EXT_NO = "red_user_signature_ext_no";

	/**
	 * 用户通道短信模板
	 */
	public static final String RED_USER_PASSAGE_MESSAGE_TEMPLATE = "red_user_passage_message_template";

	/**
	 * 针对下行短信已发送，待网关回执信息记录相关关联关系（sid,msg_id,mobile,create_time）
	 */
	public static final String RED_READY_MESSAGE_WAIT_RECEIPT = "ready_message_wait_receipt";

	/**
	 * redis中 消息是否需要推送KEY前缀(结构采用HASH)
	 */
	public static final String RED_READY_MT_PUSH_CONFIG = "ready_mt_push_config";

	/**
	 * redis中消息状态回执成功（通道回执）但我方对接解析失败，需要留底以备重新 更新数据
	 */
	public static final String RED_MESSAGE_STATUS_RECEIPT_EXCEPTION_LIST = "exception_message_receipt_status";

	// **************加入分布式名称定义 edit by zhengying 20170726 ************/

	/**
	 * 针对下行短信回执状态已回,但推送状态未知情况(一般是回执状态回来过快而 SUBMIT消息还未入库) 分布式
	 */
	public static String RED_QUEUE_SMS_DELIVER_FAILOVER = "queue_sms_deliver_failover";

	/**
	 * 短信下行 已回执状态，待推送给下家客户队列信息（分布式）
	 */
	public static String RED_QUEUE_SMS_MT_WAIT_PUSH = "queue_sms_mt_wait_push";

	/**
	 * redis中消息上行（通道回执）但我方对接解析失败，需要留底以备重新 更新数据
	 */
	public static final String RED_MESSAGE_MO_RECEIPT_EXCEPTION_LIST = "message_mo_receipt_exception_list";

	// ** -----------------------------------异步DB 相关（分布式）
	// ----------------------------------------------**/

//    /**
//     * redis中 待持久化主任务信息
//     */
//    public static String RED_DB_MESSAGE_TASK_LIST = "db_message_task_list";

	/**
	 * 待持久化短信提交信息
	 */
//    public static String RED_DB_MESSAGE_SUBMIT_LIST = "db_message_submit_list";

	/**
	 * 短信回执信息(下行状态)
	 */
//    public static String RED_DB_MESSAGE_STATUS_RECEIVE_LIST = "db_message_status_receive_list";

	/**
	 * 短信下行推送信息
	 */
//    public static String RED_DB_MESSAGE_MT_PUSH_LIST = "db_message_mt_push_list";

	// **------------------------------------------------------------------------------------------------**/

	// ** -----------------------------------手机号码防护墙 相关
	// ----------------------------------------------**/

	/**
	 * 同一用户统一短信模板统一手机号码每天计数器
	 */
	public static final String RED_MOBILE_GREEN_TABLES = "red_mobile_green_tables";

	// ** -----------------------------------广播通知数据 相关
	// ----------------------------------------------**/
	/**
	 * 手机号码黑名单订阅频道定义
	 */
	public static final String BROADCAST_MOBILE_BLACKLIST_TOPIC = "broadcast_mobile_blacklist_topic";

	/**
	 * 短信模板订阅频道定义
	 */
	public static final String BROADCAST_MESSAGE_TEMPLATE_TOPIC = "broadcast_message_template_topic";

	/**
	 * 可用通道订阅频道定义
	 */
	public static final String BROADCAST_PASSAGE_ACCESS_TOPIC = "broadcast_passage_access_topic";
}
