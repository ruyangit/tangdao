/**
 *
 */
package com.tangdao.developer.web.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.core.constant.CommonContext.PlatformType;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.model.constant.UserBalanceConstant;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.service.IUserBalanceService;

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
	private IUserBalanceService userBalanceService;

	/**
	 * TODO 短信校验
	 * @param smsSendDTO
	 * @param ip
	 * @return
	 * @throws ValidateException
	 */
	public SmsSendDTO validate(SmsSendDTO smsSendDTO, String ip) throws ValidateException {
		// 校验时间戳是否失效
		this.authorizationValidator.validateTimestampExpired(smsSendDTO.getTimestamp());
		// 校验开发者身份的有效性
		String userCode = this.authorizationValidator.checkIdentityValidity(smsSendDTO, ip, smsSendDTO.getMobile());
		// 校验用户短信余额是否满足
		this.checkBalanceAvaiable(userCode, smsSendDTO);
		// 写入IP
		smsSendDTO.setIp(ip);
		smsSendDTO.setUserCode(userCode);
		return smsSendDTO;
	}

	/**
	 * TODO 验证签名（携带手机号码签名模式）
	 * 
	 * @param smsSendRequest
	 * @param passportModel
	 * @return
	 * @throws ValidateException
	 */
	private void checkBalanceAvaiable(String userCode, SmsSendDTO smsSendDTO) throws ValidateException {
		// 获取本次短信内容计费条数
		int fee = userBalanceService.calculateSmsAmount(userCode, smsSendDTO.getContent());
		if (UserBalanceConstant.CONTENT_WORDS_EXCEPTION_COUNT_FEE == fee) {
			throw new ValidateException(CommonApiCode.DEV7100111);
		}

		// 总手机号码数量
		int mobiles = smsSendDTO.getMobile().split(",").length;

		// 计费总条数
		int totalFee = fee * mobiles;

		// 此处需加入是否为后付款，如果为后付则不需判断余额
		// f.用户余额不足（通过计费微服务判断，结合4.1.6中的用户计费规则）
		boolean balanceEnough = userBalanceService.isBalanceEnough(userCode,
				PlatformType.SEND_MESSAGE_SERVICE, (double) totalFee);
		if (!balanceEnough) {
			throw new ValidateException(CommonApiCode.DEV7100111);
		}
		
		smsSendDTO.setFee(fee);
		smsSendDTO.setTotalFee(totalFee);
	}
}
