/**
 *
 */
package com.tangdao.developer.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tangdao.core.CommonResponse;
import com.tangdao.developer.model.dto.SmsSendDTO;

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

	@RequestMapping(value = "/send", method = { RequestMethod.POST, RequestMethod.GET })
	public CommonResponse send(SmsSendDTO smsSendDTO) {
		
		return null;
	}
}
