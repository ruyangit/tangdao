package com.tangdao.core.config.rabbit.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tangdao.core.config.rabbit.AbstartRabbitListener;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.core.context.CommonContext.CMCP;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.context.ParameterContext;
import com.tangdao.core.model.domain.SmsMtMessageDeliver;
import com.tangdao.core.model.domain.SmsMtMessageSubmit;
import com.tangdao.core.model.domain.SmsPassageAccess;
import com.tangdao.core.service.SmsMtMessageDeliverService;
import com.tangdao.core.service.SmsMtMessageSubmitService;
import com.tangdao.core.service.SmsPassageAccessService;
import com.tangdao.core.service.proxy.ISmsProviderService;

import cn.hutool.core.collection.CollUtil;

/**
 * 
 * <p>
 * TODO 短信下行状态报告回执处理
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class SmsWaitReceiptListener extends AbstartRabbitListener {

	@Autowired
	private SmsMtMessageSubmitService smsMtSubmitService;
	@Reference
	private ISmsProviderService smsProviderService;
	@Autowired
	private SmsMtMessageDeliverService smsMtDeliverService;
	@Autowired
	private Jackson2JsonMessageConverter messageConverter;
	@Autowired
	private SmsPassageAccessService smsPassageAccessService;

	@Override
	@RabbitListener(queues = RabbitConstant.MQ_SMS_MT_WAIT_RECEIPT)
	public void onMessage(Message message, Channel channel) throws Exception {

		checkIsStartingConsumeMessage();

		try {
			Object object = messageConverter.fromMessage(message);
			// 处理待提交队列逻辑
			if (object == null) {
				logger.warn("状态回执报告解析失败：回执数据为空");
				return;
			}

			List<SmsMtMessageDeliver> delivers;
			if (object instanceof JSONObject) {
				delivers = doDeliverMessage((JSONObject) object);
			} else if (object instanceof List) {
				ObjectMapper mapper = new ObjectMapper();
				delivers = mapper.convertValue(object, new TypeReference<List<SmsMtMessageDeliver>>() {
				});
			} else {
				logger.error("状态回执数据类型无法匹配");
				return;
			}

			if (CollUtil.isEmpty(delivers)) {
				logger.error("状态回执报告解析为空 : {}", messageConverter.fromMessage(message));
				return;
			}

			// 处理回执完成后的持久操作
			smsMtDeliverService.doFinishDeliver(delivers);

		} catch (Exception e) {
			// 需要做重试判断
			logger.error("MQ消费网关状态回执数据失败： {}", messageConverter.fromMessage(message), e);
			backupIfFailed(message, e.getMessage());

			// channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,
			// false);
		} finally {
			// 确认消息成功消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	/**
	 * 如果队列消费失败则持久化到异常队列中
	 *
	 * @param message  队列消息
	 * @param errorDes 错误描述信息
	 */
	private void backupIfFailed(Message message, String errorDes) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("reason", "上家回执状态报告失败:" + errorDes);
		jsonObject.put("message", messageConverter.fromMessage(message));
		smsMtDeliverService.doDeliverToException(jsonObject);
	}

	/**
	 * TODO 状态报告推送处理逻辑
	 *
	 * @param jsonObject
	 * @return
	 */
	private List<SmsMtMessageDeliver> doDeliverMessage(JSONObject jsonObject) {
		// 提供商代码（通道）
		String providerCode = jsonObject.getString(ParameterContext.PASSAGE_PROVIDER_CODE_NODE);
		if (StringUtils.isEmpty(providerCode)) {
			logger.warn("上家推送状态回执报告解析失败：回执码为空");
			jsonObject.put("reason", "上家推送状态回执报告解析失败：回执码为空");
			smsMtDeliverService.doDeliverToException(jsonObject);
			return null;
		}

		SmsPassageAccess access = smsPassageAccessService.getByType(PassageCallType.MT_STATUS_RECEIPT_WITH_PUSH,
				providerCode);
		if (access == null) {
			logger.warn("上家推送状态回执报告通道参数无法匹配：{}", jsonObject.toJSONString());
			jsonObject.put("reason", "上家推送状态回执报告通道参数无法匹配");
			smsMtDeliverService.doDeliverToException(jsonObject);
			return null;
		}

		// 回执数据解析后的报文
		List<SmsMtMessageDeliver> delivers = smsProviderService.receiveMtReport(access, jsonObject);
		if (CollUtil.isEmpty(delivers)) {
			return null;
		}

		fillMobileWhenMobileMissed(delivers);

		return delivers;
	}

	/**
	 * TODO 如果报文回执中手机号码为空，则需要根据MSG_ID去数据库查询提交数据中对应的手机号码填充
	 * PS：此处可能存在上家报文回执过快，我方未入库情况，这时候需要异常处理
	 *
	 * @param delivers
	 */
	private void fillMobileWhenMobileMissed(List<SmsMtMessageDeliver> delivers) {
		SmsMtMessageSubmit submit;
		for (SmsMtMessageDeliver deliver : delivers) {
			if (StringUtils.isNotBlank(deliver.getMobile())) {
				continue;
			}
			submit = smsMtSubmitService.getByMsgid(deliver.getMsgId());
			deliver.setMobile(submit.getMobile());
			deliver.setCmcp(CMCP.local(submit.getMobile()).getCode());
		}
	}
}
