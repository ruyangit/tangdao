/**
 *
 */
package com.tangdao.developer.web.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.core.constant.UserBalanceConstant;
import com.tangdao.core.context.CommonContext.PlatformType;
import com.tangdao.core.exception.BusinessException;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.service.UserBalanceService;

/**
 * <p>
 * TODO 描述
 * </p>
 *
 * @author ruyang
 * @since 2021年2月23日
 */
@Component
public class SmsSendValidator {

	@Autowired
	private AuthorizationValidator authorizationValidator;

	@Autowired
	private UserBalanceService userBalanceService;

	/**
	 * TODO 短信校验
	 * 
	 * @param smsSendDTO
	 * @return
	 * @throws BusinessException
	 */
	public SmsSendDTO validate(SmsSendDTO smsSendDTO) throws BusinessException {
		// 校验时间戳是否失效
		this.authorizationValidator.validateTimestampExpired(smsSendDTO.getTimestamp());
		// 校验开发者身份的有效性
		String userId = this.authorizationValidator.checkIdentityValidity(smsSendDTO, smsSendDTO.getIp(),
				smsSendDTO.getMobile());
		// 校验用户短信余额是否满足
		this.checkBalanceAvaiable(userId, smsSendDTO.getAppId(), smsSendDTO);
		// 写入用户编码
		smsSendDTO.setUserId(userId);
		return smsSendDTO;
	}

	/**
	 * TODO 验证签名（携带手机号码签名模式）
	 * 
	 * @param userCode
	 * @param smsSendDTO
	 * @return
	 * @throws BusinessException
	 */
	private void checkBalanceAvaiable(String userId, String appId, SmsSendDTO smsSendDTO) throws BusinessException {
		// 获取本次短信内容计费条数
		int fee = userBalanceService.calculateSmsAmount(appId, smsSendDTO.getContent());
		if (UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE == fee) {
			throw new ValidateException(CommonApiCode.DEV7100111);
		}

		// 总手机号码数量
		int mobiles = smsSendDTO.getMobile().split(",").length;

		// 计费总条数
		int totalFee = fee * mobiles;

		// 此处需加入是否为后付款，如果为后付则不需判断余额
		// f.用户余额不足（通过计费微服务判断，结合4.1.6中的用户计费规则）
		boolean balanceEnough = userBalanceService.isBalanceEnough(userId, PlatformType.SEND_MESSAGE_SERVICE,
				(double) totalFee);
		if (!balanceEnough) {
			throw new ValidateException(CommonApiCode.DEV7100111);
		}

		smsSendDTO.setFee(fee);
		smsSendDTO.setTotalFee(totalFee);
	}
}
