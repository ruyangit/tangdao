package com.tangdao.developer.validator.sms;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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
import com.tangdao.developer.request.sms.SmsP2PSendRequest;
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
public class SmsP2PValidator extends Validator {

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
	public SmsP2PSendRequest validate(Map<String, String[]> paramMap, String ip) throws ValidateException {

		SmsP2PSendRequest smsP2PSendRequest = new SmsP2PSendRequest();
		super.validateAndParseFields(smsP2PSendRequest, paramMap);

		// 获取授权通行证实体
		AuthorizationRequest passportModel = passportValidator.validate(paramMap, ip);

		smsP2PSendRequest.setIp(ip);
		smsP2PSendRequest.setUserId(passportModel.getUserId());

		// 点对点短信如果内容为空，则返回错误码
		String body = smsP2PSendRequest.getBody();
		if (StrUtil.isEmpty(body)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_BODY_IS_WRONG);
		}

		List<JSONObject> p2pBodies = JSON.parseObject(body, new TypeReference<List<JSONObject>>() {
		});

		// 移除节点为空数据
		removeElementWhenNodeIsEmpty(p2pBodies);

		if (CollectionUtils.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_BODY_IS_WRONG);
		}

		P2pBalance p2pBalance;
		try {
			// 获取本次短信内容计费条数
			p2pBalance = userBalanceService.calculateP2pSmsAmount(passportModel.getUserId(), p2pBodies);
			if (p2pBalance == null || p2pBalance.getTotalFee() == 0) {
				throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
			}
		} catch (Exception e) {
			logger.error("开发者中心计费错误", e);
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_EXCEPTION);
		}

		// f.用户余额不足（通过计费微服务判断，结合4.1.6中的用户计费规则）
		boolean balanceEnough = userBalanceService.isBalanceEnough(passportModel.getUserId(),
				PlatformType.SEND_MESSAGE_SERVICE, (double) p2pBalance.getTotalFee());
		if (!balanceEnough) {
			throw new ValidateException(CommonApiCode.COMMON_BALANCE_NOT_ENOUGH);
		}

		smsP2PSendRequest.setFee(p2pBalance.getTotalFee());
		smsP2PSendRequest.setTotalFee(p2pBalance.getTotalFee());
		smsP2PSendRequest.setIp(ip);
		smsP2PSendRequest.setUserId(passportModel.getUserId());
		smsP2PSendRequest.setP2pBodies(p2pBalance.getP2pBodies());

		return smsP2PSendRequest;
	}

	/**
	 * TODO 移除数据为空数据
	 * 
	 * @param p2pBodies
	 */
	private void removeElementWhenNodeIsEmpty(List<JSONObject> p2pBodies) throws ValidateException {
		if (CollUtil.isEmpty(p2pBodies)) {
			throw new ValidateException(SmsApiCode.SMS_P2P_BODY_IS_WRONG);
		}

		List<JSONObject> removeBodies = new ArrayList<>();
		for (JSONObject obj : p2pBodies) {
			if (StrUtil.isEmpty(obj.getString("mobile")) || StrUtil.isEmpty(obj.getString("content"))) {
				logger.error("点对点短信内容或者手机号码为空，移除，JSON内容：{}", obj.toJSONString());
				removeBodies.add(obj);
			}
		}

		if (CollUtil.isNotEmpty(removeBodies)) {
			p2pBodies.removeAll(removeBodies);
		}
	}

}
