package com.tangdao.exchanger.web.config.rabbit.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.tangdao.core.config.rabbit.AbstartRabbitListener;
import com.tangdao.exchanger.service.PassageMessageTemplateService;
import com.tangdao.exchanger.service.PassageService;
import com.tangdao.exchanger.service.SmsProviderService;
import com.tangdao.exchanger.service.SmsSignatureExtnoService;

/**
 * 
 * <p>
 * TODO 短信待提交队列监听
 * </p>
 *
 * @author ruyang
 * @since 2021年3月8日
 */
@Component
public class SmsWaitSubmitListener extends AbstartRabbitListener {

	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	@Autowired
	private SmsProviderService smsProviderService;
	@Autowired
	private PushConfigService pushConfigService;
	@Autowired
	private MobileLocalService mobileLocalService;
	@Autowired
	private UserSmsConfigService userSmsConfigService;
	@Autowired
	private SmsMtSubmitService smsSubmitService;
	@Autowired
	private PassageService smsPassageService;
	@Autowired
	private SmsSignatureExtnoService smsSignatureExtnoService;
	@Autowired
	private Jackson2JsonMessageConverter messageConverter;
	@Autowired
	private PassageMessageTemplateService smsPassageMessageTemplateService;
	@Autowired
    private ThreadPoolTaskExecutor            threadPoolTaskExecutor;

	/**
	 * 处理分包产生的数据，并调用上家通道接口
	 *
	 * @param packets 子任务
	 */
	private void transport2Gateway(SmsMtTaskPackets packets) {
		if (StringUtils.isBlank(packets.getMobile())) {
			throw new RuntimeException("手机号码数据包为空，无法解析");
		}

		if (Integer.valueOf(packets.getStatus()) == PacketsApproveStatus.WAITING.getCode()
				|| Integer.valueOf(packets.getStatus()) == PacketsApproveStatus.REJECT.getCode()) {
			logger.info("子任务状态为待处理或驳回，不处理");
			return;
		}

		// 查询推送地址信息
		PushConfig pushConfig = pushConfigService.getPushUrl(packets.getUserCode(),
				CallbackUrlType.SMS_STATUS.getCode(), packets.getCallback());

		try {
			// 组装最终发送短信的扩展号码
			String extNumber = getUserExtNumber(packets.getUserCode(), packets.getTemplateExtNumber(),
					packets.getExtNumber(), packets.getContent());

			// 获取通道信息
			SmsPassage smsPassage = smsPassageService.findById(packets.getFinalPassageId());

			// 重新调整扩展号码
			extNumber = resizeExtNumber(extNumber, smsPassage);

			// 根据网关分包数要求对手机号码进行拆分，分批提交
			List<String> groupMobiles = regroupMobileByPacketsSize(packets.getMobile(), smsPassage);

			// add by zhengying 20179610 加入签名自动前置后置等逻辑
			packets.setContent(changeMessageContentBySignMode(packets.getContent(), packets.getPassageSignMode()));

			for (String groupMobile : groupMobiles) {
				if (StringUtils.isEmpty(groupMobile)) {
					continue;
				}

				// 调用网关通道处理器，提交短信信息，并接收回执
				List<ProviderSendResponse> responses = smsProviderService.sendSms(
						getPassageParameter(packets, smsPassage), groupMobile, packets.getContent(),
						packets.getSingleFee(), extNumber);

				// ProviderSendResponse response = list.iterator().next();
				List<SmsMtMessageSubmit> list = makeSubmitReport(packets, groupMobile, responses, extNumber,
						pushConfig);
				if (ListUtils.isEmpty(list)) {
					logger.error("解析上家回执数据逻辑数据为空，伪造包逻辑处理");
					continue;
				}

				persistSubmitMessage(list);
			}

		} catch (Exception e) {
			logger.error("调用上家通道失败", e);
			sendMqueueIfFailed(packets, packets.getMobile(), pushConfig);
		}
	}

	/**
	 * 获取用户的拓展号码
	 *
	 * @param userCode          用户
	 * @param templateExtNumber 短信模板扩展号码
	 * @param extNumber         用户自定义扩展号码
	 * @return
	 */
	private String getUserExtNumber(String userCode, String templateExtNumber, String extNumber, String content) {

		// 签名扩展号码（1对1），优先级最高，add by 20170709
		String signExtNumber = smsSignatureExtnoService.getExtNumber(userCode, content);
		if (signExtNumber == null) {
			signExtNumber = "";
		}

		// 如果短信模板扩展名不为空，则按照此扩展号码为主（忽略用户短信配置的扩展号码）
		if (StringUtils.isNotEmpty(templateExtNumber)) {
			return signExtNumber + templateExtNumber + (StringUtils.isEmpty(extNumber) ? "" : extNumber);
		}

		// 如果签名扩展号码不为空，并且模板扩展号码为空，则以扩展号码为主（忽略用户短信配置的扩展号码）
		if (StringUtils.isNotEmpty(signExtNumber)) {
			return signExtNumber + (StringUtils.isEmpty(extNumber) ? "" : extNumber);
		}

		if (userCode == null) {
			return extNumber;
		}

		UserSmsConfig userSmsConfig = userSmsConfigService.getByUserCode(userCode);
		if (userSmsConfig == null) {
			return extNumber;
		}

		if (StringUtils.isEmpty(userSmsConfig.getExtNumber())) {
			return extNumber;
		}

		return userSmsConfig.getExtNumber() + (StringUtils.isEmpty(extNumber) ? "" : extNumber);
	}

	/**
	 * 截取超出通道扩展号最大长度的位数
	 *
	 * @param extNumber  扩展号码
	 * @param smsPassage 通道信息
	 */
	private String resizeExtNumber(String extNumber, SmsPassage smsPassage) {
		if (StringUtils.isEmpty(extNumber)) {
			return extNumber;
		}

		// 如果扩展号码
		if (smsPassage == null || PASSAGE_EXT_NUMBER_LENGTH_ENDLESS == smsPassage.getExtNumber()) {
			return extNumber;
		} else if (PASSAGE_EXT_NUMBER_LENGTH_NOT_ALLOWED == smsPassage.getExtNumber()) {
			return "";
		} else {
			// add by zhengying 2017-2-50
			// 如果当前扩展号码总长度小于扩展号长度上限则在直接返回，否则按照扩展号上限截取
			return extNumber.length() < smsPassage.getExtNumber() ? extNumber
					: extNumber.substring(0, smsPassage.getExtNumber());
		}
	}

	/**
	 * 短信签名前缀符号
	 */
	private static final String MESSAGE_SIGNATURE_PRIFIX = "【";

	/**
	 * 短信签名后缀符号
	 */
	private static final String MESSAGE_SIGNATURE_SUFFIX = "】";

	/**
	 * 扩展号码长度无限
	 */
	private static final int PASSAGE_EXT_NUMBER_LENGTH_ENDLESS = -1;

	/**
	 * 扩展号码不可扩展
	 */
	private static final int PASSAGE_EXT_NUMBER_LENGTH_NOT_ALLOWED = 0;

	/**
	 * 根据签名模式调整短信内容（主要针对签名位置）
	 *
	 * @param content  短信内容
	 * @param signMode 签名模型
	 * @return
	 */
	private static String changeMessageContentBySignMode(String content, Integer signMode) {
		if (StringUtils.isEmpty(content)) {
			return null;
		}

		if (signMode == null || PassageSignMode.IGNORED.getValue() == signMode) {
			return content;
		}

		if (PassageSignMode.SIGNATURE_AUTO_PREPOSITION.getValue() == signMode) {
			// 自动前置
			if (content.endsWith(MESSAGE_SIGNATURE_SUFFIX)) {
				return content.substring(content.lastIndexOf(MESSAGE_SIGNATURE_PRIFIX))
						+ content.substring(0, content.lastIndexOf(MESSAGE_SIGNATURE_PRIFIX));
			}

		} else if (PassageSignMode.SIGNATURE_AUTO_POSTPOSITION.getValue() == signMode) {
			// 自动后置
			if (content.startsWith(MESSAGE_SIGNATURE_PRIFIX)) {
				return content.substring(content.indexOf(MESSAGE_SIGNATURE_SUFFIX) + 1, content.length())
						+ content.substring(0, content.indexOf(MESSAGE_SIGNATURE_SUFFIX) + 1);
			}

		} else if (PassageSignMode.REMOVE_SIGNATURE.getValue() == signMode) {
			// 自动去签名
			if (content.startsWith(MESSAGE_SIGNATURE_PRIFIX)) {
				content = content.substring(content.indexOf(MESSAGE_SIGNATURE_SUFFIX) + 1, content.length());
			}

			if (content.endsWith(MESSAGE_SIGNATURE_SUFFIX)) {
				content = content.substring(0, content.lastIndexOf(MESSAGE_SIGNATURE_PRIFIX));
			}

		}

		return content;
	}

	/**
	 * 转换获取通道参数信息
	 *
	 * @param packets    分包信息
	 * @param smsPassage 通道信息
	 * @return 通道参数
	 */
	private SmsPassageParameter getPassageParameter(SmsMtTaskPackets packets, SmsPassage smsPassage) {
		SmsPassageParameter parameter = new SmsPassageParameter();
		parameter.setProtocol(packets.getPassageProtocol());
		parameter.setParams(packets.getPassageParameter());
		parameter.setUrl(packets.getPassageUrl());
		parameter.setSuccessCode(packets.getSuccessCode());
		parameter.setResultFormat(packets.getResultFormat());
		parameter.setPosition(packets.getPosition());
		parameter.setPassageId(packets.getFinalPassageId());
		parameter.setPacketsSize(packets.getPassageSpeed());

		if (smsPassage == null) {
			return parameter;
		}

		// add by 20170831 加入最大连接数和连接超时时间限制（目前主要用于HTTP请求）
		parameter.setConnectionSize(smsPassage.getConnectionSize());
		parameter.setReadTimeout(smsPassage.getReadTimeout());

		if (smsPassage.getWordNumber() != UserBalanceConstant.WORDS_SIZE_PER_NUM) {
			parameter.setFeeByWords(smsPassage.getWordNumber());
		}

		// add by 20170918 判断通道是否为强制参数模式
		if (PassageSmsTemplateParam.NO.getValue() == smsPassage.getSmsTemplateParam()) {
			return parameter;
		}

		logger.info("通道：{} 为携参通道", smsPassage.getId());

		// 根据短信内容查询通道短信模板参数
		SmsPassageMessageTemplate smsPassageMessageTemplate = smsPassageMessageTemplateService
				.getByMessageContent(smsPassage.getId(), packets.getContent());
		if (smsPassageMessageTemplate == null) {
			logger.warn("通道：{} 短信模板参数信息匹配为空", smsPassage.getId());
			return parameter;
		}

		// 针对通道方指定模板ID及模板内变量名称数据模式设置参数
		parameter.setSmsTemplateId(smsPassageMessageTemplate.getTemplateId());
		parameter.setVariableParamNames(smsPassageMessageTemplate.getParamNames().split(","));

		// 根据表达式和参数数量获取本次具体的变量值
		parameter.setVariableParamValues(PassageMessageTemplateService.pickupValuesByRegex(packets.getContent(),
				smsPassageMessageTemplate.getRegexValue(),
				smsPassageMessageTemplate.getParamNames().split(",").length));

		return parameter;
	}

	/**
	 * 处理提交完成逻辑
	 *
	 * @param submits 提交记录集合信息
	 */
	private void persistSubmitMessage(List<SmsMtMessageSubmit> submits) {
		try {
			smsSubmitService.batchInsertSubmit(submits);

			// 判断并设置推送信息
			smsSubmitService.setPushConfigurationIfNecessary(submits);

		} catch (Exception e) {
			logger.error("处理待提交信息REDIS失败，失败信息：{}", JSON.toJSONString(submits), e);
		}
	}

	/**
	 * 组装提交完成的短息信息入库
	 * 
	 * @param packets    子任务
	 * @param mobileStr  以逗号分隔的手机号码字符串（可能多个手机号码，也可能单个）
	 * @param responses  回执信息集合
	 * @param extNumber  扩展号码
	 * @param pushConfig 推送信息
	 * @return 提交集合信息
	 */
	private List<SmsMtMessageSubmit> makeSubmitReport(SmsMtTaskPackets packets, String mobileStr,
			List<ProviderSendResponse> responses, String extNumber, PushConfig pushConfig) {
		String[] mobiles = mobileStr.split(",");
		if (mobiles.length == 0) {
			return null;
		}

		SmsMtMessageSubmit submitTemplate = makeMessageSubmitTemplate(packets, pushConfig, extNumber);

		List<SmsMtMessageSubmit> submits = new ArrayList<>();

		// 批量获取手机号码省份归属地
		Map<String, AreaLocal> mobileProvinceLocals = mobileLocalService.getByMobiles(mobiles);

		for (String mobile : mobiles) {

			SmsMtMessageSubmit submit = new SmsMtMessageSubmit();

			BeanUtils.copyProperties(submitTemplate, submit);
			submit.setMobile(mobile);
			submit.setCmcp(mobileProvinceLocals.get(mobile).getCmcp());
			submit.setAreaCode(mobileProvinceLocals.get(mobile).getAreaCode());

			fillSubmitFromResponse(submit, responses, packets.getSid());

			// 如果提交数据失败，则需要制造伪造包补推送
			if (MessageSubmitStatus.FAILED.getCode() == Integer.valueOf(submit.getStatus())) {
				rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_SMS, RabbitConstant.MQ_SMS_MT_PACKETS_EXCEPTION,
						submit);
				continue;
			}
			submits.add(submit);
		}

		return submits;
	}

	/**
	 * 生成短消息提交模板记录
	 * 
	 * @param packets    子任务
	 * @param pushConfig 推送信息
	 * @param extNumber  扩展名
	 * @return 短信提交记录
	 */
	private SmsMtMessageSubmit makeMessageSubmitTemplate(SmsMtTaskPackets packets, PushConfig pushConfig,
			String extNumber) {
		SmsMtMessageSubmit submitTemplate = new SmsMtMessageSubmit();

		// 排除子任务中的通道ID（要以最终通道为准 : finalPassageId）
		// mobile子任务中是以逗号隔开的多个手机号码，submit需要分开赋值
		BeanUtils.copyProperties(packets, submitTemplate, "passageId", "mobile");
		submitTemplate.setPassageId(packets.getFinalPassageId());

		// 推送信息为固定地址或者每次传递地址才需要推送
		if (pushConfig != null && Integer.valueOf(pushConfig.getStatus()) != PushConfigStatus.NO.getCode()) {
			submitTemplate.setPushUrl(pushConfig.getUrl());
			submitTemplate.setNeedPush(true);
		}

		submitTemplate.setCreateTime(new Date());
		submitTemplate.setDestnationNo(extNumber);
		// 设置提交消息的id为null ruyang
//        submitTemplate.setId(null);
		submitTemplate.setKey(null);
		return submitTemplate;
	}

	/**
	 * 根据回执信息填充submit
	 * 
	 * @param submit    提交数据
	 * @param responses 网关回执
	 * @param sid       消息ID
	 */
	private void fillSubmitFromResponse(SmsMtMessageSubmit submit, List<ProviderSendResponse> responses, Long sid) {
		// 回执数据可能为空（直连协议常见）
		if (ListUtils.isEmpty(responses)) {
			submit.setStatus(MessageSubmitStatus.FAILED.getCode() + "");
			submit.setPushErrorCode(SmsPushCode.SMS_SUBMIT_PASSAGE_FAILED.getCode());
			submit.setRemarks(SmsPushCode.SMS_SUBMIT_PASSAGE_FAILED.getCode());
			submit.setMsgId(sid + "");
			return;
		}

		int effect = 0;
		for (ProviderSendResponse response : responses) {
			// 回执手机号码如果为空，则表明网关回执不携带手机号码直接赋值状态相关
			if (StringUtils.isNotEmpty(response.getMobile()) && !submit.getMobile().equals(response.getMobile())) {
				continue;
			}

			submit.setStatus(response.isSuccess() ? MessageSubmitStatus.SUCCESS.getCode() + ""
					: MessageSubmitStatus.FAILED.getCode() + "");
			// 如果通道发送失败，则设置伪造包状态码S0013 add by 20180908
			if (!response.isSuccess()) {
				submit.setPushErrorCode(OpenApiCode.SmsPushCode.SMS_PASSAGE_AUTH_NOT_MATCHED.getCode());
			}

			submit.setRemarks(response.getRemark());
			submit.setMsgId(StringUtils.isNotEmpty(response.getSid()) ? response.getSid() : sid + "");

			effect++;
		}

		// 如果最终一条都未匹配上，则任务调用错误，理论上不会发生，除非上家通道提交回执错乱
		if (effect == 0) {
			submit.setStatus(MessageSubmitStatus.FAILED.getCode() + "");
			submit.setPushErrorCode(SmsPushCode.SMS_SUBMIT_PASSAGE_FAILED.getCode());
			submit.setRemarks(SmsPushCode.SMS_SUBMIT_PASSAGE_FAILED.getCode());
			submit.setMsgId(sid + "");
		}
	}

	/**
	 * 提交短信至上家通道（发送网关错误，组装伪造包S0099）
	 *
	 * @param packets      子任务
	 * @param mobileReport 手机号码信息
	 * @param pushConfig   推送信息
	 */
	private void sendMqueueIfFailed(SmsMtTaskPackets packets, String mobileReport, PushConfig pushConfig) {

		SmsMtMessageSubmit submitTemplate = makeMessageSubmitTemplate(packets, pushConfig, null);

		submitTemplate.setStatus(MessageSubmitStatus.FAILED.getCode() + "");
		submitTemplate.setRemarks(SmsPushCode.SMS_SUBMIT_PASSAGE_FAILED.getCode());
		submitTemplate.setMsgId(packets.getSid() + "");

		String[] mobiles = mobileReport.split(",");
		for (String mobile : mobiles) {
			SmsMtMessageSubmit submit = new SmsMtMessageSubmit();
			BeanUtils.copyProperties(submitTemplate, submit);
			submit.setCmcp(CMCP.local(mobile).getCode());
			submit.setMobile(mobile);

			rabbitTemplate.convertAndSend(RabbitConstant.EXCHANGE_SMS, RabbitConstant.MQ_SMS_MT_PACKETS_EXCEPTION,
					submit);
		}
	}

	/**
	 * 待提交短信处理
	 *
	 * @param message 队列信息
	 * @param channel 频道
	 */
	@Override
	public void onMessage(Message message, Channel channel) throws Exception {

		checkIsStartingConsumeMessage();

		try {
			Object object = messageConverter.fromMessage(message);
			// 针对 人工审核处理，重新入队列，入队列数据为子包
			if (object instanceof SmsMtTaskPackets) {
				transport2Gateway((SmsMtTaskPackets) object);
			} else {
				transport2Gateway((SmsMtTask) object);
			}

		} catch (Exception e) {
			logger.error("MQ消费提交网关数据失败： {}", messageConverter.fromMessage(message), e);
			// channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,
			// false);
		} finally {
			// 确认消息成功消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	/**
	 * 主任务（多个子任务）发送网关
	 *
	 * @param task 主任务
	 */
	private void transport2Gateway(SmsMtTask task) {
		if (task == null) {
			logger.error("待提交队列解析失败，主任务为空");
			return;
		}

		List<SmsMtTaskPackets> list = task.getPackets();
		for (SmsMtTaskPackets packet : list) {
			if (Integer.valueOf(packet.getStatus()) == PacketsApproveStatus.WAITING.getCode()
					|| Integer.valueOf(packet.getStatus()) == PacketsApproveStatus.REJECT.getCode()) {
				logger.info("数据包待处理，无需本次分包处理");
				continue;
			}

			if (StringUtils.isEmpty(packet.getMobile())) {
				logger.warn("待提交队列处理异常：手机号码为空， 跳出 {}", JSON.toJSONString(packet));
				continue;
			}

			transport2Gateway(packet);
		}
	}

	/**
	 * 根据通道分包数重组手机号码
	 * 
	 * @param mobile     手机号码
	 * @param smsPassage 短信通道
	 * @return 重组后的手机号码
	 */
	private static List<String> regroupMobileByPacketsSize(String mobile, SmsPassage smsPassage) {
		if (StringUtils.isBlank(mobile)) {
			throw new RuntimeException("提交任务错误：手机号码为空");
		}

		String[] mobiles = mobile.split(MobileCatagory.MOBILE_SPLIT_CHARCATOR);
		if (mobiles.length == 1) {
			return Arrays.asList(mobiles);
		}

		// 如果通道为空或者分包手机号码未配置按照 分包队列的默认数直接提交（默认4000）
		if (smsPassage == null || smsPassage.getMobileSize() == 0
				|| smsPassage.getMobileSize() == DEFAULT_REQUEST_MOBILE_PACKAGE_SIZE) {
			return regroupMobiles(mobiles, mobiles.length);
		}

		return regroupMobiles(mobiles, smsPassage.getMobileSize());
	}

}
