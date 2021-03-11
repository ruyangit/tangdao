package com.tangdao.developer.validator.sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tangdao.core.constant.OpenApiCode.CommonApiCode;
import com.tangdao.core.constant.OpenApiCode.SmsApiCode;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.model.vo.P2pBalance;
import com.tangdao.core.service.UserBalanceService;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.request.AuthorizationRequest;
import com.tangdao.developer.request.sms.SmsP2PTemplateSendRequest;
import com.tangdao.developer.validator.AuthorizationValidator;
import com.tangdao.developer.validator.Validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年3月11日
 */
@Component
public class SmsP2PTemplateValidator extends Validator {

	@Autowired
	private AuthorizationValidator passportValidator;

	@Autowired
	private UserBalanceService userBalanceService;

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
		smsP2PTemplateSendRequest.setUserId(passportModel.getUserId());

		// 点对点短信如果内容为空，则返回错误码
		String body = smsP2PTemplateSendRequest.getBody();
		if (StrUtil.isEmpty(body)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		List<JSONObject> p2pBodies = JSON.parseObject(body, new TypeReference<List<JSONObject>>() {
		});

		// 移除节点为空数据
		removeElementWhenNodeIsEmpty(p2pBodies);

		if (CollUtil.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		P2pBalance p2pBalance = null;
		try {
			// 获取本次短信内容计费条数
			p2pBalance = userBalanceService.calculateP2ptSmsAmount(passportModel.getUserId(),
					paramMap.get("content")[0], p2pBodies);

			if (p2pBalance == null || p2pBalance.getTotalFee() == 0) {
				throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
			}
		} catch (Exception e) {
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
		}

		// f.用户余额不足（通过计费微服务判断，结合4.1.6中的用户计费规则）
		boolean balanceEnough = userBalanceService.isBalanceEnough(passportModel.getUserId(),
				PlatformType.SEND_MESSAGE_SERVICE, (double) p2pBalance.getTotalFee());
		if (!balanceEnough) {
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_NOT_ENOUGH);
		}

		smsP2PTemplateSendRequest.setFee(p2pBalance.getTotalFee());
		smsP2PTemplateSendRequest.setTotalFee(p2pBalance.getTotalFee());
		smsP2PTemplateSendRequest.setIp(ip);
		smsP2PTemplateSendRequest.setUserId(passportModel.getUserId());
		smsP2PTemplateSendRequest.setP2pBodies(p2pBalance.getP2pBodies());

		return smsP2PTemplateSendRequest;
	}

	/**
	 * TODO 移除数据为空数据
	 * 
	 * @param p2pBodies
	 * @throws ValidateException
	 */
	private void removeElementWhenNodeIsEmpty(List<JSONObject> p2pBodies) throws ValidateException {
		if (CollUtil.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_TEMPLATE_BODY_IS_WRONG);
		}

		List<JSONObject> removeBodies = new ArrayList<>();
		for (JSONObject obj : p2pBodies) {
			if (obj.get("args") == null || StrUtil.isEmpty(obj.getString("mobile"))) {
				logger.error("点对点模板短信内容或者手机号码为空，移除，JSON内容：{}", obj.toJSONString());
				removeBodies.add(obj);
			}
		}

		if (CollUtil.isNotEmpty(removeBodies)) {
			p2pBodies.removeAll(removeBodies);
		}
	}

}
