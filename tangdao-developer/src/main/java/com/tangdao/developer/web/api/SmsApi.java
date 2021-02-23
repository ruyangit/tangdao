/**
 *
 */
package com.tangdao.developer.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.CommonResponse;
import com.tangdao.core.constant.CommonApiCode;
import com.tangdao.developer.exception.ValidateException;
import com.tangdao.developer.model.dto.SmsSendDTO;
import com.tangdao.developer.model.vo.SmsSendVo;
import com.tangdao.developer.web.validate.SmsSendValidator;

/**
 * <p>
 * TODO 短信接口
 * </p>
 *
 * @author ruyang
 * @since 2021年2月22日
 */
@RestController
@RequestMapping(value = "/sms")
public class SmsApi extends BaseApi {

	@Autowired
	private SmsSendValidator smsValidator;

	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse send(SmsSendDTO smsSendDTO) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		try {
			smsSendDTO = this.smsValidator.validate(smsSendDTO, getClientIp());
			smsSendDTO.setAppType(getAppType());
			SmsSendVo sendVo = new SmsSendVo();
			return commonResponse.setData(sendVo);
		} catch (ValidateException e) {
			return null;
		} catch (Exception e) {
			log.error("用户短信发送失败", e);
			return commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
		}
	}
}
