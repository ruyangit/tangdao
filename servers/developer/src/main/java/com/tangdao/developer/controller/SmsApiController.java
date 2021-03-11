package org.tangdao.developer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tangdao.common.constant.OpenApiCode.CommonApiCode;
import org.tangdao.common.exception.ValidateException;
import org.tangdao.developer.prervice.SmsPrervice;
import org.tangdao.developer.request.AuthorizationRequest;
import org.tangdao.developer.request.sms.SmsP2PSendRequest;
import org.tangdao.developer.request.sms.SmsP2PTemplateSendRequest;
import org.tangdao.developer.request.sms.SmsSendRequest;
import org.tangdao.developer.response.sms.SmsBalanceResponse;
import org.tangdao.developer.response.sms.SmsSendResponse;
import org.tangdao.developer.validator.sms.SmsP2PTemplateValidator;
import org.tangdao.developer.validator.sms.SmsP2PValidator;
import org.tangdao.developer.validator.sms.SmsValidator;

import com.alibaba.fastjson.JSON;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(value = "/sms")
public class SmsApiController extends BasicApiSupport {

	@Autowired
	private SmsPrervice smsPrervice;
	@Autowired
	private SmsValidator smsValidator;
	@Autowired
	private SmsP2PValidator smsP2PValidator;
	@Autowired
	private SmsP2PTemplateValidator smsP2PTemplateValidator;

	/**
	 * TODO 发送短信
	 *
	 * @return
	 */
	@ApiOperation(value = "单条/批量短信发送", notes = "接口说明", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "appkey", value = "用户接口账号", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "appsecret", value = "接口签名(接口密码、手机号、时间戳32位MD5加密生成),MD5(password+mobile+timestamp)", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "mobile", value = "手机号码(多个号码之间用半角逗号隔开，最大不超过1000个号码)", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "content", value = "短信内容", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "timestamp", value = "时间戳，短信发送当前时间毫秒数，生成数字签名用，有效时间30秒", required = true, dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "extNumber", value = "扩展号，必须为数字", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "attach", value = "自定义信息，状态报告如需推送，将携带本数据一同推送", dataType = "String", paramType = "query"),
			@ApiImplicitParam(name = "callback", value = "自定义状态报告推送地址，如果用户推送地址设置为固定推送地址，则本值无效，以系统绑定的固定地址为准，否则以本地址为准", dataType = "String", paramType = "query") })
	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public SmsSendResponse send() {
		try {
			SmsSendRequest smsSendRequest = smsValidator.validate(request.getParameterMap(), getClientIp());
			smsSendRequest.setAppType(getAppType());

			return smsPrervice.sendMessage(smsSendRequest);

		} catch (ValidateException e) {
			return saveInvokeFailedRecord(e.getMessage());
		} catch (Exception e) {
			logger.error("用户短信发送失败", e);
			return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
		}
	}

	/**
	 * TODO 普通点对点提交短信
	 *
	 * @return
	 */
	@RequestMapping(value = "/p2p", method = { RequestMethod.POST, RequestMethod.GET })
	public SmsSendResponse p2pSend() {
		try {
			SmsP2PSendRequest smsP2PSendRequest = smsP2PValidator.validate(request.getParameterMap(), getClientIp());
			smsP2PSendRequest.setAppType(getAppType());

			return smsPrervice.sendP2PMessage(smsP2PSendRequest);

		} catch (ValidateException e) {
			return saveInvokeFailedRecord(e.getMessage());
		} catch (Exception e) {
			logger.error("用户普通点对点短信发送失败", e);
			return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
		}
	}

	/**
	 * TODO 保存错误信息
	 *
	 * @param message
	 * @return
	 */
	private SmsSendResponse saveInvokeFailedRecord(String message) {
		SmsSendResponse response = new SmsSendResponse(JSON.parseObject(message));
		try {
			// 如果处理失败则持久化到DB
			smsPrervice.saveErrorLog(response.getCode(), request.getRequestURL().toString(), getClientIp(),
					request.getParameterMap(), getAppType());
		} catch (Exception e) {
			// 暂时忽略日志打印
			// logger.error("持久化提交接口错误失败", e);
		}

		return response;
	}

	/**
	 * TODO 模板点对点提交短信
	 *
	 * @return
	 */
	@RequestMapping(value = "/p2p_template", method = { RequestMethod.POST, RequestMethod.GET })
	public SmsSendResponse p2pTemplateSend() {
		try {
			SmsP2PTemplateSendRequest smsP2PTemplateSendRequest = smsP2PTemplateValidator
					.validate(request.getParameterMap(), getClientIp());
			smsP2PTemplateSendRequest.setAppType(getAppType());

			return smsPrervice.sendP2PTemplateMessage(smsP2PTemplateSendRequest);

		} catch (Exception e) {
			logger.error("用户模板点对点短信发送失败", e);

			if (e instanceof ValidateException) {
				return saveInvokeFailedRecord(e.getMessage());
			}

			return new SmsSendResponse(CommonApiCode.COMMON_SERVER_EXCEPTION);
		}
	}

	/**
	 * TODO 获取余额
	 *
	 * @return
	 */
	@RequestMapping(value = "/balance", method = { RequestMethod.POST, RequestMethod.GET })
	public SmsBalanceResponse getBalance() {
		try {
			AuthorizationRequest model = passportValidator.validate(request.getParameterMap(), getClientIp());
			model.setAppType(getAppType());

			return smsPrervice.getBalance(model.getUserCode());
		} catch (Exception e) {
			e.printStackTrace();
			// 如果失败则存储错误日志
			String code = CommonApiCode.COMMON_SERVER_EXCEPTION.getCode();
			if (e instanceof ValidateException) {
				SmsSendResponse response = saveInvokeFailedRecord(e.getMessage());
				if (response != null) {
					code = response.getCode();
				}
			}
			return new SmsBalanceResponse(code);
		}

	}
}
