package com.tangdao.exchanger.resolver.sms.http;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.tangdao.common.lang.DateUtils;
import org.tangdao.modules.exchanger.template.vo.TParameter;
import org.tangdao.modules.sms.model.domain.SmsMoMessageReceive;
import org.tangdao.modules.sms.model.domain.SmsMtMessageDeliver;

/**
 * TODO HTTP基础处理器
 *
 * @version V1.0
 * @date 2018年01月27日 下午10:07:37
 */
public abstract class AbstractPassageResolver implements SmsHttpPassageResolver {

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	/**
	 * 通道简码对应的处理器实体类关系
	 */
	private static Map<String, SmsHttpPassageResolver> CODE_REFRENCE_BEANS = new HashMap<>();

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 下行状态HTTP状态报告REDIS前置（主要用于状态回执报告中没有手机号码， 顾发送短信需要提前设置MSG_ID和MOBILE对应关系）
	 */
	private static final String REDIS_MT_REPORT_HTTP_PRIFIX_KEY = "mt_http_map";

	/**
	 * 公共状态回执成功码
	 */
	public static final String COMMON_MT_STATUS_SUCCESS_CODE = "DELIVRD";

	/**
	 * 多个手机号码分隔符
	 */
	protected static final String MULTI_MOBILES_SEPERATOR = ",";

	/**
	 * 默认中文编码
	 */
	protected static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * SMS路径
	 */
	private static final String SMS_ROUTER = "sms/";

	/**
	 * TODO 初始化通道简码对应的实体映射
	 */
	@PostConstruct
	protected void loadCodeRefrenceBeans() {
		if (CODE_REFRENCE_BEANS.containsKey(getCode())) {
			throw new RuntimeException("Passage's code[" + getCode() + "] is exists in current factory");
		}

		try {
			CODE_REFRENCE_BEANS.put(getCode(), this);
			logger.info("Loading http passage's code[" + getCode() + "]-ref[" + this + "] success");
		} catch (Exception e) {
			logger.error("Loading http passage's code[" + getCode() + "]-ref[" + this + "] failed", e);
		}
	}

	/**
	 * TODO 根据通道简码获取相关处理器,当子类重写父类的方法，在调用父类此方法时会执行子类重写方法
	 *
	 * @param code 通道简码
	 * @return
	 */
	public static SmsHttpPassageResolver getInstance(String code) {
		SmsHttpPassageResolver instance = CODE_REFRENCE_BEANS.get(getCode(code));
		if (instance == null) {
			throw new RuntimeException("Passage's custom code[" + getCode(code) + "] can't find any reference");
		}

		return instance;
	}

	/**
	 * TODO 下行状态报告回执(推送)
	 *
	 * @param report
	 * @return
	 */
	public List<SmsMtMessageDeliver> mtDeliver(String report, String successCode) {
		throw new UnsupportedOperationException("It needs implement by child class");
	}

	/**
	 * TODO 下行状态报告回执（自取）
	 *
	 * @param tparameter
	 * @param url
	 * @param successCode
	 * @return
	 */
	public List<SmsMtMessageDeliver> mtDeliver(TParameter tparameter, String url, String successCode) {
		throw new UnsupportedOperationException("It needs implement by child class");
	}

	/**
	 * TODO 上行短信状态回执
	 *
	 * @param report
	 * @return
	 */
	public List<SmsMoMessageReceive> moReceive(String report, String passageId) {
		throw new UnsupportedOperationException("It needs implement by child class");
	}

	/**
	 * TODO 上行短信状态回执
	 *
	 * @param tparameter
	 * @param url
	 * @param passageId
	 * @return
	 */
	public List<SmsMoMessageReceive> moReceive(TParameter tparameter, String url, String passageId) {
		throw new UnsupportedOperationException("It needs implement by child class");
	}

	/**
	 * TODO 处理器简码（必须唯一）
	 *
	 * @return
	 */
	protected abstract String code();

	/**
	 * TODO unixtime 转时间戳
	 *
	 * @param timestampString
	 * @return
	 */
	protected static String unixtimeStamp2Date(Object timestampString) {
		if (timestampString == null || StringUtils.isEmpty(timestampString.toString())) {
			return DateUtils.getDateTime();
		}

		try {
			Long timestamp = Long.parseLong(timestampString.toString()) * 1000;
			return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(timestamp));
		} catch (Exception e) {
			return DateUtils.getDateTime();
		}
	}

	/**
	 * TODO 时间数字格式（yyyyMMddHHmmss，如20110115105822）转换格式为yyyy-MM-dd HH:mm:ss 字符
	 *
	 * @param dataNumber
	 * @return
	 */
	protected static String dateNumberFormat(Object dataNumber) {
		return dateNumberFormat(dataNumber, null);
	}

	protected static String dateNumberFormat(Object dataNumber, String format) {
		if (dataNumber == null || StringUtils.isEmpty(dataNumber.toString())) {
			return DateUtils.getDateTime();
		}

		try {
			SimpleDateFormat ff = new java.text.SimpleDateFormat(
					StringUtils.isEmpty(format) ? "yyyyMMddHHmmss" : format);
			return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(ff.parse(dataNumber.toString()));
		} catch (Exception e) {
			return DateUtils.getDateTime();
		}
	}

	/**
	 * TODO 获取HTTP通道发送 消息ID对应手机号码REDIS KEY
	 *
	 * @param msgId
	 * @return
	 */
	private String getRedisMtMsgIdKey(String msgId) {
		return String.format("%s:%s:%s", REDIS_MT_REPORT_HTTP_PRIFIX_KEY, getCode(), msgId);
	}

	/**
	 * TODO 设置发送报告MSG_ID和手机号码对应关系至REDIS
	 *
	 * @param msgId
	 * @param mobile
	 */
	protected void setReportMsgIdWithMobile(String msgId, String mobile) {
		try {
			stringRedisTemplate.opsForValue().set(getRedisMtMsgIdKey(msgId), mobile);
		} catch (Exception e) {
			logger.error("Redis 设置发送状态MSG_ID: {} 和MOBILE : {} 对应关系失败", msgId, mobile, e);
		}
	}

	/**
	 * TODO 获取发送报告MSG_ID和手机号码对应关系至REDIS
	 *
	 * @param msgId
	 * @return
	 */
	protected String getReportMsgIdWithMobile(String msgId) {
		try {
			Object obj = stringRedisTemplate.opsForValue().get(getRedisMtMsgIdKey(msgId));
			if (obj == null) {
				logger.error("Redis 获取发送状态MSG_ID: {} 数据为空", msgId);
				return null;
			}

			return obj.toString();
		} catch (Exception e) {
			logger.error("Redis 获取发送状态MSG_ID: {} 和MOBILE对应关系失败", msgId, e);
			return null;
		}
	}

	/**
	 * TODO 移除发送报告MSG_ID和手机号码对应关系至REDIS
	 *
	 * @param msgId
	 */
	protected void removeReportMsgIdWithMobile(String msgId) {
		try {
			stringRedisTemplate.delete(getRedisMtMsgIdKey(msgId));
		} catch (Exception e) {
			logger.error("Redis 移除发送状态MSG_ID: {} 和MOBILE对应关系失败", msgId, e);
		}
	}

	/**
	 * TODO 获取最终的CODE（加路由）
	 * 
	 * @return
	 */
	private String getCode() {
		return SMS_ROUTER + code();
	}

	private static String getCode(String code) {
		return SMS_ROUTER + code;
	}

}
