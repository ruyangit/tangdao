package org.tangdao.developer.validator.sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.tangdao.common.constant.CommonContext.PlatformType;
import org.tangdao.common.constant.OpenApiCode.CommonApiCode;
import org.tangdao.common.constant.OpenApiCode.SmsApiCode;
import org.tangdao.common.exception.ValidateException;
import org.tangdao.developer.request.AuthorizationRequest;
import org.tangdao.developer.request.sms.SmsP2PTemplateSendRequest;
import org.tangdao.developer.validator.AuthorizationValidator;
import org.tangdao.developer.validator.Validator;
import org.tangdao.modules.sys.model.vo.P2pBalanceResponse;
import org.tangdao.modules.sys.service.IUserBalanceService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

@Component
public class SmsP2PTemplateValidator extends Validator {

	@Autowired
	private AuthorizationValidator passportValidator;

	@Autowired
	private IUserBalanceService userBalanceService;

	/**
	 * TODO 用户参数完整性校验
	 * 
	 * @param paramMap
	 * @param ip
	 * @return
	 * @throws ValidateException
	 */
	public SmsP2PTemplateSendRequest validate(Map<String, String[]> paramMap, String ip) throws ValidateException {

		SmsP2PTemplateSendRequest smsP2PTemplateSendRequest = new SmsP2PTemplateSendRequest();
		super.validateAndParseFields(smsP2PTemplateSendRequest, paramMap);

		// 获取授权通行证实体
		AuthorizationRequest passportModel = passportValidator.validate(paramMap, ip);

		smsP2PTemplateSendRequest.setIp(ip);
		smsP2PTemplateSendRequest.setUserCode(passportModel.getUserCode());

		// 点对点短信如果内容为空，则返回错误码
		String body = smsP2PTemplateSendRequest.getBody();
		if (StringUtils.isEmpty(body)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		List<JSONObject> p2pBodies = JSON.parseObject(body, new TypeReference<List<JSONObject>>() {
		});

		// 移除节点为空数据
		removeElementWhenNodeIsEmpty(p2pBodies);

		if (CollectionUtils.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		P2pBalanceResponse p2pBalanceResponse = null;
		try {
			// 获取本次短信内容计费条数
			p2pBalanceResponse = userBalanceService.calculateP2ptSmsAmount(passportModel.getUserCode(),
					paramMap.get("content")[0], p2pBodies);

			if (p2pBalanceResponse == null || p2pBalanceResponse.getTotalFee() == 0) {
				throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
			}
		} catch (Exception e) {
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
		}

		// f.用户余额不足（通过计费微服务判断，结合4.1.6中的用户计费规则）
		boolean balanceEnough = userBalanceService.isBalanceEnough(passportModel.getUserCode(),
				PlatformType.SEND_MESSAGE_SERVICE, (double) p2pBalanceResponse.getTotalFee());
		if (!balanceEnough) {
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_NOT_ENOUGH);
		}

		smsP2PTemplateSendRequest.setFee(p2pBalanceResponse.getTotalFee());
		smsP2PTemplateSendRequest.setTotalFee(p2pBalanceResponse.getTotalFee());
		smsP2PTemplateSendRequest.setIp(ip);
		smsP2PTemplateSendRequest.setUserCode(passportModel.getUserCode());
		smsP2PTemplateSendRequest.setP2pBodies(p2pBalanceResponse.getP2pBodies());

		return smsP2PTemplateSendRequest;
	}

	/**
	 * TODO 移除数据为空数据
	 * 
	 * @param p2pBodies
	 * @throws ValidateException
	 */
	private void removeElementWhenNodeIsEmpty(List<JSONObject> p2pBodies) throws ValidateException {
		if (CollectionUtils.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		List<JSONObject> removeBodies = new ArrayList<>();
		for (JSONObject obj : p2pBodies) {
			if (obj.get("args") == null || StringUtils.isEmpty(obj.getString("mobile"))) {
				logger.error("点对点模板短信内容或者手机号码为空，移除，JSON内容：{}", obj.toJSONString());
				removeBodies.add(obj);
			}
		}

		if (CollectionUtils.isNotEmpty(removeBodies)) {
			p2pBodies.removeAll(removeBodies);
		}
	}

}
