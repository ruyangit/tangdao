package com.tangdao.exchanger.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.tangdao.core.constant.SmsRedisConstant;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO  手机号码防火墙设置服务实现
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Service
public class SmsMobileTablesService{

	// 手机号码最后发送毫秒时间
	private static final String MOBILE_LAST_SEND_MILLIS = "last_send_millis";
	// 手机号码发送总数
	private static final String MOBILE_SEND_TOTAL_COUNT = "send_total_count";
	
	public static final int NICE_PASSED = 0;
	
	public static final int MOBILE_BEYOND_SPEED = 1;
	
	public static final int MOBILE_BEYOND_TIMES = 2;

	@Resource
	private StringRedisTemplate stringRedisTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * TODO 组合生成辅助KEY
	 * 
	 * @param keyname
	 * @param userId
	 * @param templateId
	 * @param mobile
	 * @return
	 */
	private String getAssistKey(String keyname, String userCode, String templateId, String mobile) {
		return String.format("%s:%s:%s:%s", keyname, userCode, templateId, mobile);
	}

	/**
	 * 
	 * TODO 获取距离次日0点的毫秒时间差(REDIS TTL自动过期)
	 * 
	 * @return
	 */
	private static long getH24TimeMillis() {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			
			Date endDate = dfs.parse(DateUtil.formatDate(DateUtil.offsetDay(new Date(), -1)) + " 00:00:00.000");

			return endDate.getTime() - System.currentTimeMillis();
		} catch (Exception e) {
			// 默认6小时
			return System.currentTimeMillis() + 6 * 60 * 60 * 1000;
		}
	}

	public void setMobileSendRecord(String userCode, String templateId, String mobile, int sendCount) {
		if (StrUtil.isEmpty(userCode) || templateId == null || StrUtil.isEmpty(mobile)) {
			logger.error("参数数据为空，无法匹配该用户：{}, 模板：{}，手机号码：{} ", userCode, templateId, mobile);
			return;
		}

		try {
			String key = getAssistKey(SmsRedisConstant.RED_MOBILE_GREEN_TABLES, userCode, templateId, mobile);

			Map<Object, Object> map = new HashMap<>();
			map.put(MOBILE_LAST_SEND_MILLIS, System.currentTimeMillis() + "");
			map.put(MOBILE_SEND_TOTAL_COUNT, sendCount + "");

			stringRedisTemplate.opsForHash().putAll(key, map);
			stringRedisTemplate.expire(key, getH24TimeMillis(), TimeUnit.MILLISECONDS);

		} catch (Exception e) {
			logger.error("REDIS 手机号防火墙设置失败，请优先处理", e);
		}
	}

	public int checkMobileIsBeyondExpected(String userCode, String templateId, String mobile, int maxSpeed,
			int maxLimit) {

		Integer _sendTotalCount = null;
		try {
			// 如果上限次数为0，则不允许提交任何
			if (maxLimit == 0) {
				return MOBILE_BEYOND_TIMES;
			}

			// 根据手机号码和用户信息获取手机防火墙计数信息
			Map<Object, Object> map = stringRedisTemplate.opsForHash()
					.entries(getAssistKey(SmsRedisConstant.RED_MOBILE_GREEN_TABLES, userCode, templateId, mobile));

			if (CollUtil.isEmpty(map)) {
				setMobileSendRecord(userCode, templateId, mobile,
						_sendTotalCount == null ? 0 : _sendTotalCount.intValue());
				return NICE_PASSED;
			}

			// 当前发送的总量
			Object sendTotalCount = map.get(MOBILE_SEND_TOTAL_COUNT);
			_sendTotalCount = Integer.parseInt(sendTotalCount.toString()) + 1;

			// 判断手机号码一天内发送总量是否已超过预设值
			if (_sendTotalCount >= maxLimit) {
				return MOBILE_BEYOND_TIMES;
			}

			// 如果提交频率为0，则不限制提交任何
			if (maxSpeed == 0) {
				setMobileSendRecord(userCode, templateId, mobile,
						_sendTotalCount == null ? 0 : _sendTotalCount.intValue());
				return NICE_PASSED;
			}

			// 判断 手机号码上次发送时间距离当前时间否已超过预设值
			Object lastSendMillis = map.get(MOBILE_LAST_SEND_MILLIS);
			if (System.currentTimeMillis() - Long.parseLong(lastSendMillis.toString()) < maxSpeed * 1000) {
				return MOBILE_BEYOND_SPEED;
			}

			setMobileSendRecord(userCode, templateId, mobile, _sendTotalCount == null ? 0 : _sendTotalCount.intValue());
		} catch (Exception e) {
			logger.warn("REDIS查询手机号码白名单失败，数据默认通过", e);
		}
		return NICE_PASSED;
	}

}
