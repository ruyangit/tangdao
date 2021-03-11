package org.tangdao.developer.prervice;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tangdao.common.collect.MapUtils;
import org.tangdao.common.constant.CommonContext.PlatformType;
import org.tangdao.common.constant.OpenApiCode.CommonApiCode;
import org.tangdao.common.exception.QueueProcessException;
import org.tangdao.common.utils.IdGenerator;
import org.tangdao.developer.constant.RabbitConstant;
import org.tangdao.developer.constant.RabbitConstant.WordsPriority;
import org.tangdao.developer.request.sms.SmsP2PSendRequest;
import org.tangdao.developer.request.sms.SmsP2PTemplateSendRequest;
import org.tangdao.developer.request.sms.SmsSendRequest;
import org.tangdao.developer.response.sms.SmsBalanceResponse;
import org.tangdao.developer.response.sms.SmsSendResponse;
import org.tangdao.modules.sms.constant.SmsTaskContext.TaskSubmitType;
import org.tangdao.modules.sms.model.domain.SmsApiFailedRecord;
import org.tangdao.modules.sms.model.domain.SmsMtTask;
import org.tangdao.modules.sms.service.ISmsApiFailedRecordService;
import org.tangdao.modules.sys.model.domain.UserBalance;
import org.tangdao.modules.sys.service.IUserBalanceService;

import com.alibaba.fastjson.JSON;

/**
 * 
 * <p>
 * TODO 描述 短信前置服务
 * </p>
 *
 * @author ruyangit@gmail.com
 * @since 2020年3月11日
 */
@Service
public class SmsPrervice {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private RabbitTemplate smsRabbitTemplate;
	
	@Autowired
	private IUserBalanceService userBalanceService;
	
	@Autowired
	private ISmsApiFailedRecordService smsApiFailedRecordService;

	/**
	 * 发送短信信息
	 *
	 * @param smsSendRequest 发送请求
	 * @return 处理回执
	 */
	public SmsSendResponse sendMessage(SmsSendRequest smsSendRequest) {
		SmsMtTask task = new SmsMtTask();

		BeanUtils.copyProperties(smsSendRequest, task);
		task.setAppType(smsSendRequest.getAppType());

		try {

			long sid = joinTask2Queue(task);
			if (sid != 0L) {
				return new SmsSendResponse(smsSendRequest.getTotalFee(), sid);
			}

		} catch (QueueProcessException e) {
			logger.error("发送短信至队列错误， {}", e);
		}

		return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
	}

	/**
	 * 发送点对点短信信息
	 *
	 * @param smsP2PSendRequest 点对点请求
	 * @return 处理回执
	 */
	public SmsSendResponse sendP2PMessage(SmsP2PSendRequest smsP2PSendRequest) {
		SmsMtTask task = new SmsMtTask();

		BeanUtils.copyProperties(smsP2PSendRequest, task);
		task.setAppType(smsP2PSendRequest.getAppType());
		task.setSubmitType(TaskSubmitType.POINT_TO_POINT.getCode());
		task.setP2pBody(smsP2PSendRequest.getBody());
		task.setP2pBodies(smsP2PSendRequest.getP2pBodies());

		try {

			long sid = joinTask2Queue(task);
			if (sid != 0L) {
				return new SmsSendResponse(smsP2PSendRequest.getTotalFee(), sid);
			}

		} catch (QueueProcessException e) {
			logger.error("发送短信至队列错误， {}", e);
		}

		return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
	}

	/**
	 * 发送模板点对点短信信息
	 *
	 * @param rqeuest 点对点模板请求
	 * @return 处理结果
	 */
	public SmsSendResponse sendP2PTemplateMessage(SmsP2PTemplateSendRequest rqeuest) {
		SmsMtTask task = new SmsMtTask();

		BeanUtils.copyProperties(rqeuest, task);
		task.setAppType(rqeuest.getAppType());
		task.setSubmitType(TaskSubmitType.TEMPLATE_POINT_TO_POINT.getCode());

		try {

			long sid = joinTask2Queue(task);
			if (sid != 0L) {
				return new SmsSendResponse(rqeuest.getTotalFee(), sid);
			}

		} catch (QueueProcessException e) {
			logger.error("发送短信至队列错误， {}", e);
		}

		return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
	}

	/**
	 * 保存错误信息
	 *
	 * @param errorCode 错误代码
	 * @param url       调用URL
	 * @param ip        调用者IP
	 * @param paramMap  携带参数信息
	 */
	public void saveErrorLog(String errorCode, String url, String ip, Map<String, String[]> paramMap, int appType) {
		SmsApiFailedRecord record = new SmsApiFailedRecord();
		record.setIp(ip);
		record.setSubmitUrl(url);
		record.setRespCode(errorCode);
		// 暂时默认开发者模式
		record.setAppType(appType);
		if (MapUtils.isEmpty(paramMap)) {
			smsApiFailedRecordService.save(record);
			return;
		}

		record.setAppKey(getAttribute(paramMap, "appkey"));
		record.setAppSecret(getAttribute(paramMap, "appsecret"));
		record.setTimestamps(getAttribute(paramMap, "timestamp"));
		record.setMobile(getAttribute(paramMap, "mobile"));
		record.setContent(getAttribute(paramMap, "content"));
		record.setRemarks(JSON.toJSONString(paramMap));

		smsApiFailedRecordService.save(record);
	}

	private String getAttribute(Map<String, String[]> paramMap, String elementId) {
		return MapUtils.isEmpty(paramMap) || !paramMap.containsKey(elementId) || paramMap.get(elementId).length == 0
				? null
				: paramMap.get(elementId)[0];
	}

	/**
	 * 获取短余额信息
	 *
	 * @return 余额处理回执
	 */
	public SmsBalanceResponse getBalance(String userCode) {
		UserBalance userBalance = userBalanceService.getByUserCode(userCode, PlatformType.SEND_MESSAGE_SERVICE);
		if (userBalance == null) {
			logger.error("用户 code：{} 查询短信余额失败，用户余额数据为空", userCode);
			return new SmsBalanceResponse(CommonApiCode.COMMON_SERVER_EXCEPTION.getCode());
		}

		return new SmsBalanceResponse(CommonApiCode.COMMON_SUCCESS.getCode(), userBalance.getBalance().intValue(),
				userBalance.getPayType());
	}

	/**
	 * 提交任务到队列
	 *
	 * @param task 主任务
	 * @return 消息ID
	 */
	private long joinTask2Queue(SmsMtTask task) {
		try {
			// 更新用户余额
			boolean isSuccess = userBalanceService.deductBalance(task.getUserCode(), -task.getTotalFee(),
					PlatformType.SEND_MESSAGE_SERVICE.getCode(), "developer call");
			if (!isSuccess) {
				logger.error("用户ID: [" + task.getUserCode() + "] 扣除短信余额 " + task.getTotalFee() + " 失败");
				throw new QueueProcessException("发送短信扣除短信余额失败");
			}

			task.setSid(idGenerator.generate());
			task.setCreateTime(new Date());

			// 插入TASK任务（异步）

			// 判断队列的优先级别
			int priority = WordsPriority.getLevel(task.getContent());

			String queueName = RabbitConstant.MQ_SMS_MT_WAIT_PROCESS;
			if (TaskSubmitType.POINT_TO_POINT.getCode() == task.getSubmitType()
					|| TaskSubmitType.TEMPLATE_POINT_TO_POINT.getCode() == task.getSubmitType()) {
				queueName = RabbitConstant.MQ_SMS_MT_P2P_WAIT_PROCESS;
			}
			smsRabbitTemplate.convertAndSend(queueName, task, (message) -> {
				message.getMessageProperties().setPriority(priority);
				return message;
			}, new CorrelationData(task.getSid().toString()));

			return task.getSid();
		} catch (Exception e) {
			logger.error("发送短信队列失败", e);
			throw new QueueProcessException("发送短信队列失败");
		}
	}
}
