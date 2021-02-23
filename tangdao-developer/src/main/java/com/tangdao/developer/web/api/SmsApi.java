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
import com.tangdao.developer.service.SmsApiFaildRecordService;
import com.tangdao.developer.service.SmsSendService;
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
	
	@Autowired
	private SmsApiFaildRecordService smsApiFaildRecordService;
	
	@Autowired
	private SmsSendService smsSendService;

	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse send(SmsSendDTO smsSendDTO) {
		CommonResponse commonResponse = CommonResponse.createCommonResponse();
		try {
			smsSendDTO.setAppType(getAppType());
			smsSendDTO = this.smsValidator.validate(smsSendDTO, getClientIp());
			return commonResponse.setData(smsSendService.sendMessage(smsSendDTO));
		} catch (ValidateException e) {
			smsApiFaildRecordService.save(null);
			return null;
		} catch (Exception e) {
			log.error("用户短信发送失败", e);
			return commonResponse.fail(CommonApiCode.INTERNAL_ERROR);
		}
	}
}
