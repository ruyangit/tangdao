/**
 *
 */
package com.tangdao.core.constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.tangdao.core.model.domain.AreaLocal;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
public class RedisConstant {

	/**
	 * 用户推送配置信息
	 */
	public static final String RED_USER_PUSH_CONFIG = "red_user_push_config";

	/**
	 * 用户短信配置信息
	 */
	public static final String RED_USER_SMS_CONFIG = "red_user_sms_config";

	/**
	 * 用户彩信配置信息
	 */
	public static final String RED_USER_MMS_CONFIG = "red_user_mms_config";

	/**
	 * 用户短信通道
	 */
	public static final String RED_USER_SMS_PASSAGE = "red_user_sms_passage";

	/**
	 * 用户彩信通道
	 */
	public static final String RED_USER_MMS_PASSAGE = "red_user_mms_passage";

	/**
	 * 用户服务器IP报备信息
	 */
	public static final String RED_USER_WHITE_HOST = "red_user_white_host";

	/**
	 * 用户列表
	 */
	public static final String RED_USER_LIST = "red_user_list";

	/**
	 * 开发者列表
	 */
	public static final String RED_DEVELOPER_LIST = "red_developer_list";

	/**
	 * 省份配置
	 */
	public static final String RED_PROVINCE = "red_province";

	/**
	 * 三大运营商正则表达式
	 */
	public static final String RED_CMCP_REGEX = "red_cmcp_regex";

	/**
	 * 黑名单词库
	 */
	public static final String RED_BLACKLIST_WORDS = "red_blacklist_words";

	/**
	 * 手机归属地
	 */
	public static final String RED_AREA_MOBILES_LOCAL = "red_area_mobiles_local";

	/**
	 * 全局手机归属号码
	 */
	public static Map<String, AreaLocal> GLOBAL_MOBILES_LOCAL = new ConcurrentHashMap<>();
}
