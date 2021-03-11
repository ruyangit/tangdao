package com.tangdao.exchanger.resolver.sms.http;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.tangdao.core.context.ParameterContext;
import com.tangdao.core.model.domain.SmsMoMessageReceive;
import com.tangdao.core.model.domain.SmsMtMessageDeliver;
import com.tangdao.core.model.domain.SmsPassageAccess;
import com.tangdao.core.model.domain.SmsPassageParameter;
import com.tangdao.core.model.vo.ProviderSendVo;
import com.tangdao.exchanger.resolver.HttpClientManager;
import com.tangdao.exchanger.resolver.handler.DeliverHandler;
import com.tangdao.exchanger.resolver.handler.RequestHandler;
import com.tangdao.exchanger.resolver.handler.ResponseHandler;
import com.tangdao.exchanger.template.vo.TParameter;

import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO http 自定义处理器
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Component
public class SmsHttpSender {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * TODO 调用上家通道接口
	 *
	 * @param parameter
	 * @param mobile
	 * @param content
	 * @param extNumber
	 * @return
	 */
	public List<ProviderSendVo> post(SmsPassageParameter parameter, String mobile, String content,
			String extNumber) {
		TParameter tparameter = RequestHandler.parse(parameter.getParams());
		if (StrUtil.isNotEmpty(tparameter.customPassage())) {
			return sendCustomTranslate(parameter, mobile, content, extNumber, tparameter);
		}

		setNecesaryMessageNode(tparameter, mobile, content);

		// 转换参数，并调用网关接口，接收返回结果
		String result = HttpClientManager.post(parameter.getUrl(), tparameter);

		logger.info("发送接口返回值：{}", result);

		// 解析返回结果并返回
		return ResponseHandler.parse(result, parameter.getResultFormat(), parameter.getPosition(),
				parameter.getSuccessCode());
	}

	/**
	 * TODO 设置必要的参数设置，如手机号码，短信内容； 注：后期需要考虑扩展号码
	 *
	 * @param tparameter
	 * @param mobile
	 * @param content
	 */
	private void setNecesaryMessageNode(TParameter tparameter, String mobile, String content) {
		// 设置用户手机号
		if (tparameter.containsKey(TParameter.MOBILE_NODE_NAME)) {
			tparameter.put(tparameter.get(TParameter.MOBILE_NODE_NAME).toString(), mobile);
			tparameter.remove(TParameter.MOBILE_NODE_NAME);
		} else {
			tparameter.put(TParameter.MOBILE_NODE_NAME, mobile);
		}

		// 设置短信内容
		if (tparameter.containsKey(TParameter.CONTENT_NODE_NAME)) {
			tparameter.put(tparameter.get(TParameter.CONTENT_NODE_NAME).toString(), content);
			tparameter.remove(TParameter.CONTENT_NODE_NAME);
		} else {
			tparameter.put(TParameter.CONTENT_NODE_NAME, content);
		}

		// 设置消息ID，如果对方需要我方 设置此 消息ID（用于比对返回状态数据），则需要设置，默认与SID一致
		// PS:大部分情况不存在此数据，由网关自行设置
//		if (tparameter.containsKey(TParameter.MSGID_NODE_NAME)) {
//			tparameter.put(tparameter.get(TParameter.MSGID_NODE_NAME).toString(), model.getSid());
//			tparameter.remove(TParameter.MSGID_NODE_NAME);
//		}

		// 判断用户的拓展号码，需要结合通道是否支持拓展和 拓展的位数综合判断，判断用户传递的此参数是否为空及是否有权限拓展
//		model.getExtNumber();

	}

	/**
	 * TODO 调用自定义通道
	 *
	 * @param parameter  通道模板参数
	 * @param mobile     手机号码
	 * @param content    短信内容
	 * @param extNumber  扩展号码
	 * @param tparameter
	 * @return
	 */
	private List<ProviderSendVo> sendCustomTranslate(SmsPassageParameter parameter, String mobile, String content,
			String extNumber, TParameter tparameter) {

		return AbstractPassageResolver.getInstance(tparameter.customPassage()).send(parameter, mobile, content,
				extNumber);
	}

	/**
	 * TODO 解析回执数据报告（推送）
	 *
	 * @param access
	 * @param report
	 * @return
	 */
	public List<SmsMtMessageDeliver> deliver(SmsPassageAccess access, JSONObject report) {

		TParameter tparameter = RequestHandler.parse(access.getParams());
		if (StrUtil.isNotEmpty(tparameter.customPassage())) {
			return customStatusTranslate(report, tparameter, access.getSuccessCode());
		}

		return DeliverHandler.translate(access, report);
	}

	/**
	 * TODO 解析回执数据报告（轮训）
	 *
	 * @param access
	 * @return
	 */
	public List<SmsMtMessageDeliver> deliver(SmsPassageAccess access) {

		TParameter tparameter = RequestHandler.parse(access.getParams());
		if (StrUtil.isNotEmpty(tparameter.customPassage())) {
			return customStatusTranslate(access, tparameter);
		}

		return null;
	}

	/**
	 * TODO 用自定义通道（轮训回执解析）
	 *
	 * @param access
	 * @param tparameter
	 * @return
	 */
	private List<SmsMtMessageDeliver> customStatusTranslate(SmsPassageAccess access, TParameter tparameter) {
		return AbstractPassageResolver.getInstance(tparameter.customPassage()).mtDeliver(tparameter, access.getUrl(),
				access.getSuccessCode());
	}

	/**
	 * TODO 调用自定义通道（推送回执解析）
	 *
	 * @param report
	 * @param tparameter
	 * @param successCode
	 * @return
	 */
	private List<SmsMtMessageDeliver> customStatusTranslate(JSONObject report, TParameter tparameter,
			String successCode) {
		return AbstractPassageResolver.getInstance(tparameter.customPassage())
				.mtDeliver(report.getString(ParameterContext.PARAMETER_NAME_IN_STREAM), successCode);
	}

	/**
	 * TODO 调用自定义通道(轮训)
	 *
	 * @param access
	 * @param tparameter
	 * @return
	 */
	private List<SmsMoMessageReceive> customMoTranslate(SmsPassageAccess access, TParameter tparameter) {
		return AbstractPassageResolver.getInstance(tparameter.customPassage()).moReceive(tparameter, access.getUrl(),
				access.getPassageId());
	}

	/**
	 * TODO 调用自定义通道（推送解析）
	 *
	 * @param report
	 * @param tparameter
	 * @param passageId
	 * @return
	 */
	private List<SmsMoMessageReceive> customMoTranslate(JSONObject report, TParameter tparameter, String passageId) {
		return AbstractPassageResolver.getInstance(tparameter.customPassage())
				.moReceive(report.getString(ParameterContext.PARAMETER_NAME_IN_STREAM), passageId);
	}

	/**
	 * TODO 解析上行数据报告
	 *
	 * @param access
	 * @param report
	 * @return
	 */
	public List<SmsMoMessageReceive> mo(SmsPassageAccess access, JSONObject report) {
		TParameter tparameter = RequestHandler.parse(access.getParams());
		if (StrUtil.isNotEmpty(tparameter.customPassage())) {
			return customMoTranslate(report, tparameter, access.getPassageId());
		}

		throw new RuntimeException("未配置公共上行解析器");
	}

	/**
	 * TODO 解析上行数据报告
	 *
	 * @param access
	 * @return
	 */
	public List<SmsMoMessageReceive> mo(SmsPassageAccess access) {
		TParameter tparameter = RequestHandler.parse(access.getParams());
		if (StrUtil.isNotEmpty(tparameter.customPassage())) {
			return customMoTranslate(access, tparameter);
		}

		return null;
	}

}
