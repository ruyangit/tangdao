package com.tangdao.core.config.rabbit.listener;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Reference;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.tangdao.core.config.rabbit.AbstartRabbitListener;
import com.tangdao.core.constant.RabbitConstant;
import com.tangdao.core.context.CommonContext.PassageCallType;
import com.tangdao.core.context.ParameterContext;
import com.tangdao.core.model.domain.SmsMoMessageReceive;
import com.tangdao.core.model.domain.SmsPassageAccess;
import com.tangdao.core.service.SmsMoMessageReceiveService;
import com.tangdao.core.service.SmsPassageAccessService;
import com.tangdao.core.service.proxy.ISmsProviderService;

import cn.hutool.core.collection.CollUtil;

/**
 * 
 * <p>
 * TODO 短信上行监听
 * </p>
 *
 * @author ruyang
 * @since 2021年3月10日
 */
public class SmsMoReceiveListener extends AbstartRabbitListener {

	@Reference
	private ISmsProviderService smsProviderService;

	@Autowired
	private Jackson2JsonMessageConverter messageConverter;

	@Autowired
	private SmsMoMessageReceiveService smsMoReceiveService;

	@Autowired
	private SmsPassageAccessService smsPassageAccessService;

	@Override
	@RabbitListener(queues = RabbitConstant.MQ_SMS_MO_RECEIVE)
	public void onMessage(Message message, Channel channel) throws Exception {
		checkIsStartingConsumeMessage();

		try {
			Object object = messageConverter.fromMessage(message);
			// 处理待提交队列逻辑
			if (object == null) {
				logger.warn("上行报告解析失败：回执数据为空");
				return;
			}

			List<SmsMoMessageReceive> receives;
			if (object instanceof JSONObject) {
				receives = doReceiveMessage((JSONObject) object);
			} else if (object instanceof List) {
				ObjectMapper mapper = new ObjectMapper();
				receives = mapper.convertValue(object, new TypeReference<List<SmsMoMessageReceive>>() {
				});
			} else {
				logger.error("上行回执数据类型无法匹配");
				return;
			}

			if (CollUtil.isEmpty(receives)) {
				logger.warn("上行报告解析为空 : {}", JSON.toJSONString(message));
				return;
			}

			// 处理回执完成后的持久操作
			smsMoReceiveService.doFinishReceive(receives);

		} catch (Exception e) {
			logger.error("MQ消费网关上行数据失败： {}", messageConverter.fromMessage(message), e);
			smsMoReceiveService.doReceiveToException(message);
		} finally {
			// 确认消息成功消费
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		}
	}

	/**
	 * TODO 上行报告推送处理逻辑
	 * 
	 * @param jsonObject
	 * @return
	 */
	private List<SmsMoMessageReceive> doReceiveMessage(JSONObject jsonObject) {
		// 提供商代码（通道）
		String providerCode = jsonObject.getString(ParameterContext.PASSAGE_PROVIDER_CODE_NODE);
		if (StringUtils.isEmpty(providerCode)) {
			logger.warn("上行报告解析失败：回执码为空");
			jsonObject.put("reason", "上行报告解析失败：回执码为空");
			smsMoReceiveService.doReceiveToException(jsonObject);
			return null;
		}

		SmsPassageAccess access = smsPassageAccessService.getByType(PassageCallType.MO_REPORT_WITH_PUSH, providerCode);
		if (access == null) {
			logger.warn("上行报告通道参数无法匹配：{}", jsonObject.toJSONString());
			jsonObject.put("reason", "上行报告报告通道参数无法匹配");
			smsMoReceiveService.doReceiveToException(jsonObject);
			return null;
		}

		// 回执数据解析后的报文
		List<SmsMoMessageReceive> receives = smsProviderService.receiveMoReport(access, jsonObject);
		if (CollUtil.isEmpty(receives)) {
			return null;
		}

		return receives;
	}

}
